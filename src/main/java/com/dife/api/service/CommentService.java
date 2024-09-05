package com.dife.api.service;

import com.dife.api.exception.CommentNotFoundException;
import com.dife.api.exception.MemberException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.CommentCreateRequestDto;
import com.dife.api.model.dto.CommentResponseDto;
import com.dife.api.model.dto.MemberResponseDto;
import com.dife.api.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private final PostService postService;

	@Autowired
	@Qualifier("memberModelMapper")
	private ModelMapper memberModelMapper;

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
		MemberResponseDto memberDto = memberModelMapper.map(writer, MemberResponseDto.class);
		responseDto.setPost(postService.getPost(comment.getPost().getId(), memberEmail));
		responseDto.setWriter(memberDto);

		if (comment.getParentComment() != null) {
			responseDto.setParentComment(
					modelMapper.map(comment.getParentComment(), CommentResponseDto.class));
			translationAddChildrenComment(
					comment.getParentComment().getWriter().getSettingLanguage(), comment, post);
		}

		translationAddComment(post.getWriter().getSettingLanguage(), comment, post);

		return responseDto;
	}

	private String translationDivide(Comment comment, String settingLanguage, Boolean isChildren) {
		String username = comment.getWriter().getUsername();
		String baseMessage = "WOW!😆 " + username + " ";

		ResourceBundle resourceBundle;
		if (isChildren) {
			resourceBundle =
					ResourceBundle.getBundle("notification.addChildrenComment", Locale.getDefault());
		} else {
			resourceBundle = ResourceBundle.getBundle("notification.addComment", Locale.getDefault());
		}

		String messageSuffix = resourceBundle.getString(settingLanguage.toUpperCase());

		return baseMessage + messageSuffix;
	}

	public void translationAddChildrenComment(String settingLanguage, Comment comment, Post post) {

		List<NotificationToken> parentCommentTokens =
				comment.getParentComment().getWriter().getNotificationTokens();

		String parentMessage = translationDivide(comment, settingLanguage, true);

		addNotifications(parentCommentTokens, parentMessage, NotificationType.POST, post.getId());
	}

	public void translationAddComment(String settingLanguage, Comment comment, Post post) {

		List<NotificationToken> postTokens = post.getWriter().getNotificationTokens();

		String postMessage = translationDivide(comment, settingLanguage, false);

		addNotifications(postTokens, postMessage, NotificationType.POST, post.getId());
	}

	public CommentResponseDto getComment(Comment comment, Member member) {
		CommentResponseDto dto = modelMapper.map(comment, CommentResponseDto.class);

		dto.setWriter(memberModelMapper.map(comment.getWriter(), MemberResponseDto.class));
		dto.setPost(postService.getPost(comment.getPost().getId(), member.getEmail()));
		dto.setLikesCount(comment.getCommentLikes().size());

		if (comment.getParentComment() != null)
			dto.setParentComment(modelMapper.map(comment.getParentComment(), CommentResponseDto.class));
		if (comment.getChildrenComments() != null)
			dto.setCommentsCount(comment.getChildrenComments().size());

		dto.setIsLiked(likeCommentRepository.existsByCommentAndMember(comment, member));
		dto.setCreated(comment.getCreated());
		dto.setModified(comment.getModified());

		return dto;
	}

	@Transactional
	public void deleteComment(Long id, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);

		if (!comment.getWriter().equals(member)) {
			throw new MemberException("작성자만이 삭제를 진행할 수 있습니다!");
		}

		if (!comment.getChildrenComments().isEmpty()) {
			for (Comment childComment : comment.getChildrenComments()) {
				childComment.getPost().getComments().remove(childComment);
				commentRepository.delete(childComment);
			}
		}

		comment.getPost().getComments().remove(comment);
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
