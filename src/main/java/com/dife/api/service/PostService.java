package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.FileRepository;
import com.dife.api.repository.LikePostRepository;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
	private final FileService fileService;
	private final FileRepository fileRepository;
	private final MemberRepository memberRepository;
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
										FileDto fileDto = fileService.upload(file);
										File mappedFile = modelMapper.map(fileDto, File.class);
										mappedFile.setPost(post);
										return mappedFile;
									})
							.collect(Collectors.toList());

			fileRepository.saveAll(files);
			post.getFiles().addAll(files);
		}

		postRepository.save(post);

		return modelMapper.map(post, PostResponseDto.class);
	}

	@Transactional(readOnly = true)
	public List<PostResponseDto> getPostsByBoardType(BoardCategory boardCategory) {
		Sort sort = Sort.by(Sort.Direction.DESC, "created");
		List<Post> posts = postRepository.findPostsByBoardType(boardCategory, sort);

		return posts.stream()
				.map(
						post -> {
							PostResponseDto responseDto = modelMapper.map(post, PostResponseDto.class);
							responseDto.setCommentCount(post.getComments().size());
							responseDto.setLikesCount(post.getPostLikes().size());
							responseDto.setBookmarkCount(post.getBookmarks().size());
							return responseDto;
						})
				.collect(toList());
	}

	public PostResponseDto getPost(Long id, String memberEmail) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		PostResponseDto responseDto = modelMapper.map(post, PostResponseDto.class);
		responseDto.setCommentCount(post.getComments().size());
		responseDto.setLikesCount(post.getPostLikes().size());
		responseDto.setBookmarkCount(post.getBookmarks().size());

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
										FileDto fileDto = fileService.upload(file);
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

		return modelMapper.map(post, PostResponseDto.class);
	}

	public void deletePost(Long id, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Post post =
				postRepository.findByWriterAndId(member, id).orElseThrow(PostNotFoundException::new);

		for (File file : post.getFiles()) {
			fileService.deleteFile(file.getId());
		}

		postRepository.delete(post);
	}

	private boolean isFileValid(MultipartFile file) {
		return file != null && !file.isEmpty();
	}
}
