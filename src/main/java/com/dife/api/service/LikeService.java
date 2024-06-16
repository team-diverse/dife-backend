package com.dife.api.service;

import com.dife.api.exception.DuplicateLikeException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.repository.LikePostRepository;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
