package com.dife.api.service;

import com.dife.api.exception.CommentDuplicateException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.exception.PostNotFoundException;
import com.dife.api.model.Comment;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.dto.CommentCreateRequestDto;
import com.dife.api.model.dto.CommentResponseDto;
import com.dife.api.repository.CommentRepository;
import com.dife.api.repository.LikeCommentRepository;
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
public class CommentService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;
	private final CommentRepository commentRepository;
	private final LikeCommentRepository likeCommentRepository;

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

		if (commentRepository.existsByWriterAndPostAndParentCommentIsNull(writer, post)
				&& requestDto.getParentCommentId() == null) throw new CommentDuplicateException();

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
		if (comment.getParentComment() != null)
			responseDto.setParentComment(comment.getParentComment());
		return responseDto;
	}

	public CommentResponseDto getComment(Comment comment, Member member) {
		CommentResponseDto dto = modelMapper.map(comment, CommentResponseDto.class);

		dto.setPost(comment.getPost());
		dto.setLikesCount(comment.getCommentLikes().size());
		dto.setBookmarkCount(comment.getBookmarks().size());

		if (comment.getParentComment() != null) dto.setParentComment(comment.getParentComment());

		boolean isLiked = likeCommentRepository.existsByCommentAndMember(comment, member);
		dto.setIsLiked(isLiked);

		return dto;
	}
}
