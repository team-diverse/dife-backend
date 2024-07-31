package com.dife.api.service;

import static com.dife.api.model.LikeType.POST;
import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import com.dife.api.repository.*;
import java.util.List;
import java.util.Objects;
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

		Member writer = post.getWriter();

		if (!Objects.equals(writer.getId(), member.getId())) {
			List<NotificationToken> notificationTokens = writer.getNotificationTokens();

			for (NotificationToken notificationToken : notificationTokens) {
				Notification notification = new Notification();
				notification.setNotificationToken(notificationToken);
				notification.setType(NotificationType.COMMUNITY);
				notification.setMessage("WOW!😆 " + member.getEmail() + "님이 회원님의 게시글을 좋아해요!");
				notification.setIsRead(false);
				notificationToken.getNotifications().add(notification);
			}
		}
	}

	public void deleteLikePost(LikeCreateRequestDto dto, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		switch (dto.getType()) {
			case POST:
				Post post =
						postRepository.findById(dto.getPostId()).orElseThrow(PostNotFoundException::new);

				PostLike likePost =
						likePostRepository
								.findByPostAndMember(post, member)
								.orElseThrow(LikeNotFoundException::new);

				likePost.getPost().getPostLikes().remove(likePost);
				member.getPostLikes().remove(likePost);
				likePostRepository.delete(likePost);
				break;

			case COMMENT:
				Comment comment =
						commentRepository
								.findById(dto.getCommentId())
								.orElseThrow(CommentNotFoundException::new);

				LikeComment likeComment =
						likeCommentRepository
								.findByCommentAndMember(comment, member)
								.orElseThrow(LikeNotFoundException::new);

				likeComment.getComment().getCommentLikes().remove(likeComment);
				likeCommentRepository.delete(likeComment);
				break;
		}
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

		Member writer = comment.getWriter();

		if (!Objects.equals(writer.getId(), member.getId())) {
			List<NotificationToken> notificationTokens = writer.getNotificationTokens();

			for (NotificationToken notificationToken : notificationTokens) {
				Notification notification = new Notification();
				notification.setNotificationToken(notificationToken);
				notification.setType(NotificationType.COMMUNITY);
				notification.setMessage("WOW!😆 " + member.getEmail() + "님이 회원님의 댓글을 좋아해요!");
				notification.setIsRead(false);
				notificationToken.getNotifications().add(notification);
			}
		}
	}
}
