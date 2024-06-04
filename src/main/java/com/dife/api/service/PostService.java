package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.dto.*;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;

	public PostResponseDto createPost(PostCreateRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Post post = new Post();
		post.setTitle(requestDto.getTitle());
		post.setContent(requestDto.getContent());
		post.setIsPublic(requestDto.getIsPublic());
		post.setBoardType(requestDto.getBoardType());
		post.setMember(member);

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
	public PostResponseDto getPost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

		return modelMapper.map(post, PostResponseDto.class);
	}

	public PostResponseDto updatePost(Long id, PostUpdateRequestDto dto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Post post =
				postRepository.findByMemberAndId(member, id).orElseThrow(PostNotFoundException::new);

		post.setBoardType(dto.getBoardType());
		post.setTitle(dto.getTitle());
		post.setContent(dto.getContent());
		post.setIsPublic(dto.getIsPublic());
		postRepository.save(post);

		return modelMapper.map(post, PostResponseDto.class);
	}

	public void deletePost(Long id, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Post post =
				postRepository.findByMemberAndId(member, id).orElseThrow(PostNotFoundException::new);

		postRepository.delete(post);
	}
}
