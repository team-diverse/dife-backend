package com.dife.api.service;

import com.dife.api.exception.CommentNotFoundException;
import com.dife.api.exception.MemberException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.CommentCreateRequestDto;
import com.dife.api.model.dto.CommentResponseDto;
import com.dife.api.repository.CommentRepository;
import com.dife.api.repository.LikeCommentRepository;
import com.dife.api.repository.MemberRepository;
import com.dife.api.repository.PostRepository;
import java.time.LocalDateTime;
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
public class CommentService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;
	private final CommentRepository commentRepository;
	private final LikeCommentRepository likeCommentRepository;

	private final NotificationService notificationService;

	@Transactional(readOnly = true)
	public List<CommentResponseDto> getCommentsByPostId(Long postId, String memberEmail) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		List<Comment> comments = commentRepository.findCommentsByPost(post);

		return comments.stream()
				.map(comment -> getComment(comment, member))
				.collect(Collectors.toList());
	}

	public CommentResponseDto createComment(CommentCreateRequestDto requestDto, String memberEmail) {
		Member writer =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Post post =
				postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);

		Comment parentComment =
				(requestDto.getParentCommentId() != null)
						? commentRepository.findById(requestDto.getParentCommentId()).orElse(null)
						: null;
		Comment comment = new Comment();
		comment.setPost(post);
		comment.setParentComment(parentComment);
		comment.setWriter(writer);
		comment.setContent(requestDto.getContent());
		comment.setIsPublic(requestDto.getIsPublic());
		if (parentComment != null) {
			parentComment.getChildrenComments().add(comment);
		}

		commentRepository.save(comment);

		CommentResponseDto responseDto = modelMapper.map(comment, CommentResponseDto.class);
		if (comment.getParentComment() != null) {
			responseDto.setParentComment(comment.getParentComment());
			List<NotificationToken> parentCommentTokens =
					comment.getParentComment().getWriter().getNotificationTokens();
			String parentMessage =
					"WOW!😆 " + comment.getWriter().getUsername() + "님이 회원님이 댓글을 남긴 게시글에 다른 댓글이 추가되었어요!";
			addNotifications(parentCommentTokens, parentMessage, NotificationType.POST, post.getId());
		}

		List<NotificationToken> postTokens = post.getWriter().getNotificationTokens();
		String postMessage = "WOW!😆 " + comment.getWriter().getUsername() + "님이 회원님의 게시글에 댓글이 추가되었어요!";
		addNotifications(postTokens, postMessage, NotificationType.POST, post.getId());

		return responseDto;
	}

	public CommentResponseDto getComment(Comment comment, Member member) {
		CommentResponseDto dto = modelMapper.map(comment, CommentResponseDto.class);

		dto.setPost(comment.getPost());
		dto.setLikesCount(comment.getCommentLikes().size());

		if (comment.getParentComment() != null) dto.setParentComment(comment.getParentComment());
		if (comment.getChildrenComments() != null)
			dto.setCommentsCount(comment.getChildrenComments().size());

		boolean isLiked = likeCommentRepository.existsByCommentAndMember(comment, member);
		dto.setIsLiked(isLiked);

		return dto;
	}

	public void deleteComment(Long id, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);

		if (!comment.getWriter().equals(member)) throw new MemberException("작성자만이 삭제를 진행할 수 있습니다!");

		commentRepository.delete(comment);
	}

	private void addNotifications(
			List<NotificationToken> tokens, String message, NotificationType type, Long typeId) {
		for (NotificationToken token : tokens) {
			Notification notification = new Notification();
			notification.setNotificationToken(token);
			notification.setType(type);
			notification.setMessage(message);
			notification.setTypeId(typeId);
			notification.setCreated(LocalDateTime.now());
			token.getNotifications().add(notification);

			notificationService.sendPushNotification(
					token.getPushToken(), notification.getCreated(), message);
		}
	}
}
