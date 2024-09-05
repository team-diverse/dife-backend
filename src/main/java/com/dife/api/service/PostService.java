package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.BlockDuplicateException;
import com.dife.api.exception.MemberException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

	private final PostRepository postRepository;
	private final LikePostRepository likePostRepository;
	private final BlockPostRepository blockPostRepository;
	private final BookmarkRepository bookmarkRepository;
	private final FileService fileService;
	private final BlockService blockService;
	private final FileRepository fileRepository;
	private final MemberRepository memberRepository;

	@Autowired
	@Qualifier("memberModelMapper")
	private ModelMapper memberModelMapper;

	private final ModelMapper modelMapper;

	public PostResponseDto createPost(
			String title,
			String content,
			Boolean isPublic,
			BoardCategory boardType,
			List<MultipartFile> postFiles,
			String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Post post = new Post();
		post.setTitle(title);
		post.setContent(content);
		post.setIsPublic(isPublic);
		post.setBoardType(boardType);
		post.setWriter(member);

		postRepository.save(post);

		if (postFiles != null && !postFiles.isEmpty()) {
			List<File> files =
					postFiles.stream()
							.filter(this::isFileValid)
							.map(
									file -> {
										FileDto fileDto = null;
										fileDto = fileService.upload(file);
										File mappedFile = modelMapper.map(fileDto, File.class);
										mappedFile.setPost(post);
										return mappedFile;
									})
							.collect(Collectors.toList());

			fileRepository.saveAll(files);
			post.getFiles().addAll(files);
		}

		postRepository.save(post);

		PostResponseDto responseDto = modelMapper.map(post, PostResponseDto.class);
		responseDto.setWriter(memberModelMapper.map(post.getWriter(), MemberResponseDto.class));
		return responseDto;
	}

	@Transactional(readOnly = true)
	public List<PostResponseDto> getPostsByBoardType(BoardCategory type, String memberEmail) {
		Sort sort = Sort.by(Sort.Direction.DESC, "created");

		List<Post> posts;

		if (type == null) {
			posts = postRepository.findAll(sort);
		} else {
			posts = postRepository.findPostsByBoardType(type, sort);
		}

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Set<Member> blockedMembers = blockService.getBlackSet(member);

		return posts.stream()
				.filter(post -> !blockedMembers.contains(post.getWriter()))
				.filter(post -> !blockPostRepository.existsByPostAndMember(post, member))
				.map(post -> getPost(post.getId(), memberEmail))
				.collect(toList());
	}

	public PostResponseDto getPost(Long id, String memberEmail) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		PostResponseDto responseDto = modelMapper.map(post, PostResponseDto.class);
		responseDto.setWriter(memberModelMapper.map(post.getWriter(), MemberResponseDto.class));
		responseDto.setCommentCount(post.getComments().size());
		responseDto.setLikesCount(post.getPostLikes().size());
		responseDto.setBookmarkCount(post.getBookmarks().size());
		responseDto.setIsBookmarked(bookmarkRepository.existsBookmarkByPostAndMember(post, member));
		responseDto.setCreated(post.getCreated());
		responseDto.setModified(post.getModified());

		responseDto.setIsLiked(likePostRepository.existsByPostAndMember(post, member));

		return responseDto;
	}

	public PostResponseDto updatePost(
			Long id,
			String title,
			String content,
			Boolean isPublic,
			BoardCategory boardType,
			List<MultipartFile> postFiles,
			String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Post post =
				postRepository.findByWriterAndId(member, id).orElseThrow(PostNotFoundException::new);

		post.setTitle((title != null && !title.isEmpty()) ? title : post.getTitle());
		post.setContent((content != null && !content.isEmpty()) ? content : post.getContent());
		post.setIsPublic(isPublic != null ? isPublic : post.getIsPublic());
		post.setBoardType(boardType != null ? boardType : post.getBoardType());

		postRepository.save(post);

		if (postFiles != null && !postFiles.isEmpty()) {
			List<File> newFiles =
					postFiles.stream()
							.filter(this::isFileValid)
							.map(
									file -> {
										FileDto fileDto = null;
										fileDto = fileService.upload(file);
										File mappedFile = modelMapper.map(fileDto, File.class);
										mappedFile.setPost(post);
										return mappedFile;
									})
							.collect(Collectors.toList());

			if (!newFiles.isEmpty()) {
				fileRepository.saveAll(newFiles);
				post.getFiles().addAll(newFiles);
			}
		}

		postRepository.save(post);

		PostResponseDto responseDto = modelMapper.map(post, PostResponseDto.class);
		responseDto.setWriter(memberModelMapper.map(post.getWriter(), MemberResponseDto.class));
		return responseDto;
	}

	public void createBlock(Long postId, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		if (blockPostRepository.existsByPostAndMember(post, member))
			throw new BlockDuplicateException();

		PostBlock postBlock = new PostBlock();
		postBlock.setPost(post);
		postBlock.setMember(member);

		blockPostRepository.save(postBlock);
	}

	public void deletePost(Long id, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Post post =
				postRepository.findByWriterAndId(member, id).orElseThrow(PostNotFoundException::new);

		if (!post.getWriter().equals(member)) throw new MemberException("작성자만이 삭제를 진행할 수 있습니다!");
		for (File file : post.getFiles()) {
			fileService.deleteFile(file.getId());
		}

		postRepository.delete(post);
	}

	private boolean isFileValid(MultipartFile file) {
		return file != null && !file.isEmpty();
	}

	public List<PostResponseDto> getSearchPosts(
			String keyword, BoardCategory type, String memberEmail) {

		Member currentMember =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		String trimmedKeyword = (keyword != null) ? keyword.trim() : "";
		Sort sort = Sort.by(Sort.Direction.DESC, "created");

		List<Post> posts;

		if (type == BoardCategory.FREE || type == BoardCategory.TIP) {
			posts =
					postRepository.findPostsByBoardType(type, sort).stream()
							.filter(
									post ->
											post.getTitle().contains(trimmedKeyword)
													|| post.getContent().contains(trimmedKeyword)
													|| post.getWriter().getName().contains(trimmedKeyword))
							.collect(Collectors.toList());
		} else {
			posts = postRepository.findAllByKeywordSearch(trimmedKeyword);
		}

		Set<Member> blockedMembers = blockService.getBlackSet(currentMember);

		if (posts.isEmpty()) {
			throw new PostNotFoundException();
		}

		return posts.stream()
				.filter(post -> !blockedMembers.contains(post.getWriter()))
				.map(
						post -> {
							PostResponseDto responseDto = modelMapper.map(post, PostResponseDto.class);
							responseDto.setWriter(
									memberModelMapper.map(post.getWriter(), MemberResponseDto.class));
							responseDto.setCommentCount(post.getComments().size());
							responseDto.setLikesCount(post.getPostLikes().size());
							responseDto.setBookmarkCount(post.getBookmarks().size());
							return responseDto;
						})
				.collect(toList());
	}
}
