package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.*;
import com.dife.api.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
	private final CommentService commentService;

	@PostMapping(value = "/{postId}", consumes = "application/json")
	public ResponseEntity<CommentResponseDto> createComment(
			@RequestBody CommentCreateRequestDto requestDto,
			@PathVariable(name = "postId") Long postId,
			@RequestParam(name = "parentCommentId", required = false) Long parentCommentId,
			Authentication auth) {

		CommentResponseDto responseDto =
				commentService.createComment(requestDto, postId, parentCommentId, auth.getName());

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(
			@PathVariable(name = "postId") Long postId) {
		List<CommentResponseDto> responseDto = commentService.getCommentsByPostId(postId);
		return ResponseEntity.status(OK).body(responseDto);
	}
}
