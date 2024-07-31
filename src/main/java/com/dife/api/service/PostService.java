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
			MultipartFile postFile,
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

		if (!postFile.isEmpty()) {
			FileDto fileDto = fileService.upload(postFile);
			File file = modelMapper.map(fileDto, File.class);
			file.setPost(post);
			fileRepository.save(file);

			post.getFiles().add(file);
		}

		postRepository.save(post);

		return modelMapper.map(post, PostResponseDto.class);
	}

	@Transactional(readOnly = true)
	public List<PostResponseDto> getPostsByBoardType(BoardCategory boardCategory) {
		Sort sort = Sort.by(Sort.Direction.DESC, "created");
		List<Post> posts = postRepository.findPostsByBoardType(boardCategory, sort);

		return posts.stream().map(b -> modelMapper.map(b, PostResponseDto.class)).collect(toList());
	}

	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long id, String memberEmail) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		PostResponseDto responseDto = modelMapper.map(post, PostResponseDto.class);
		responseDto.setLikesCount(post.getPostLikes().size());
		responseDto.setBookmarkCount(post.getBookmarks().size());

		if (likePostRepository.existsByPostAndMember(post, member)) responseDto.setIsLiked(true);
		else responseDto.setIsLiked(false);

		List<File> files = post.getFiles().stream().collect(Collectors.toList());

		List<FileDto> fileDtos =
				files.stream()
						.map(
								file -> {
									FileDto fileDto = modelMapper.map(file, FileDto.class);
									fileDto.setUrl(fileService.getPresignUrl(file.getOriginalName()));
									return fileDto;
								})
						.collect(Collectors.toList());

		responseDto.setFiles(fileDtos);

		return responseDto;
	}

	public PostResponseDto updatePost(
			Long id,
			String title,
			String content,
			Boolean isPublic,
			BoardCategory boardType,
			MultipartFile postFile,
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

		if (!(post.getFiles().isEmpty() && (postFile == null || postFile.isEmpty()))) {
			if (postFile != null && !postFile.isEmpty()) {
				FileDto fileDto = fileService.upload(postFile);
				File file = modelMapper.map(fileDto, File.class);
				file.setPost(post);
				fileRepository.save(file);

				post.getFiles().add(file);
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
}
