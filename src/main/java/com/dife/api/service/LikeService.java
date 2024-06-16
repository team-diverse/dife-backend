package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import com.dife.api.repository.*;
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
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final LikePostRepository likePostRepository;
	private final LikeCommentRepository likeCommentRepository;

	private final ModelMapper modelMapper;

	public List<PostResponseDto> getLikedPosts(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		List<PostLike> postLikes = likePostRepository.findPostLikesByMember(member);

		List<Post> posts =
				postLikes.stream().map(PostLike::getPost).distinct().collect(Collectors.toList());

		return posts.stream().map(b -> modelMapper.map(b, PostResponseDto.class)).collect(toList());
	}

	public void createLike(LikeCreateRequestDto dto, String memberEmail) {
		switch (dto.getType()) {
			case POST:
				createLikePost(dto.getPostId(), memberEmail);
				break;
			case COMMENT:
				createLikeComment(dto.getCommentId(), memberEmail);
		}
	}

	public void createLikePost(Long postId, String memberEmail) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (likePostRepository.existsByPostAndMember(post, member)) {
			throw new DuplicateLikeException();
		}
		PostLike postLike = new PostLike();
		postLike.setPost(post);
		postLike.setMember(member);
		likePostRepository.save(postLike);
	}

	public void deleteLikePost(Long postId, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		PostLike postLike =
				likePostRepository
						.findByPostAndMember(post, member)
						.orElseThrow(LikeNotFoundException::new);

		postLike.getPost().getPostLikes().remove(postLike);
		member.getPostLikes().remove(postLike);
		likePostRepository.delete(postLike);
	}

	public void createLikeComment(Long commentId, String memberEmail) {
		Comment comment =
				commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (likeCommentRepository.existsByCommentAndMember(comment, member)) {
			throw new DuplicateLikeException();
		}
		LikeComment likeComment = new LikeComment();
		likeComment.setComment(comment);
		likeComment.setMember(member);
		likeCommentRepository.save(likeComment);
	}
}
