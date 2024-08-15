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
@RequestMapping("/api")
public class CommentController implements SwaggerCommentController {
	private final CommentService commentService;

	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(
			@PathVariable(name = "postId") Long postId, Authentication auth) {
		List<CommentResponseDto> responseDto =
				commentService.getCommentsByPostId(postId, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping("/comments")
	public ResponseEntity<CommentResponseDto> createComment(
			@RequestBody CommentCreateRequestDto requestDto, Authentication auth) {

		CommentResponseDto responseDto = commentService.createComment(requestDto, auth.getName());

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@DeleteMapping("/comments/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id, Authentication auth) {
		commentService.deleteComment(id, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
