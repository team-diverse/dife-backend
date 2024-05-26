package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.dto.*;
import com.dife.api.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

	private final PostRepository postRepository;
	private final ModelMapper modelMapper;

	public PostResponseDto create(PostCreateRequestDto requestDto, Member currentMember) {

		Post post = new Post();
		post.setTitle(requestDto.getTitle());
		post.setContent(requestDto.getContent());
		post.setIs_public(requestDto.getIs_public());
		post.setBoardType(requestDto.getBoardType());
		post.setMember(currentMember);

		postRepository.save(post);

		return modelMapper.map(post, PostResponseDto.class);
	}

	@Transactional(readOnly = true)
	public List<PostResponseDto> getPostsByBoardType(BoardRequestDto requestDto) {
		BoardCategory boardCategory = requestDto.getBoardCategory();
		List<Post> posts = postRepository.findPostsByBoardType(boardCategory);

		return posts.stream().map(c -> modelMapper.map(c, PostResponseDto.class)).collect(toList());
	}

	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long id) {
		Post post =
				postRepository
						.findById(id)
						.orElseThrow(() -> new PostNotFoundException("해당 게시물이 존재하지 않습니다!"));

		return modelMapper.map(post, PostResponseDto.class);
	}

	public PostResponseDto updatePost(Long id, PostUpdateRequestDto dto, Member currentMember) {
		Post post =
				postRepository
						.findByMemberAndId(currentMember, id)
						.orElseThrow(() -> new PostNotFoundException("해당 게시물이 존재하지 않습니다!"));

		post.setBoardType(dto.getBoardType());
		post.setTitle(dto.getTitle());
		post.setContent(dto.getContent());
		post.setIs_public(dto.getIs_public());
		postRepository.save(post);

		return modelMapper.map(post, PostResponseDto.class);
	}

	public void deletePost(Long id, Member currentMember) {
		Post post =
				postRepository
						.findByMemberAndId(currentMember, id)
						.orElseThrow(() -> new PostNotFoundException("해당 게시물이 존재하지 않습니다!"));

		postRepository.delete(post);
	}
}
