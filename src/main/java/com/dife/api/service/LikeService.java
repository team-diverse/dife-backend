package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.DuplicateLikeException;
import com.dife.api.exception.LikeNotFoundException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import com.dife.api.repository.LikePostRepository;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final LikePostRepository likePostRepository;

	private final ModelMapper modelMapper;

	public List<PostResponseDto> getLikedPosts(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		List<LikePost> likePosts = likePostRepository.findLikePostsByMember(member);

		List<Post> posts =
				likePosts.stream().map(LikePost::getPost).distinct().collect(Collectors.toList());

		return posts.stream().map(b -> modelMapper.map(b, PostResponseDto.class)).collect(toList());
	}

	public void createLike(LikeCreateRequestDto dto, String memberEmail) {
		switch (dto.getLikeType()) {
			case POSTLIKES:
				createLikePost(dto.getPostId(), memberEmail);
				break;
			case COMMENTLIKES:
		}
	}

	public void createLikePost(Long postId, String memberEmail) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (likePostRepository.existsByPostAndMember(post, member)) {
			throw new DuplicateLikeException();
		}
		LikePost likePost = new LikePost();
		likePost.setPost(post);
		likePost.setMember(member);
		likePostRepository.save(likePost);
	}

	public void deleteLikePost(Long postId, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		LikePost likePost =
				likePostRepository
						.findByPostAndMember(post, member)
						.orElseThrow(LikeNotFoundException::new);

		likePost.getPost().getPostLikes().remove(likePost);
		member.getPostLikes().remove(likePost);
		likePostRepository.delete(likePost);
	}
}
