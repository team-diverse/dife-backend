package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.dto.*;
import com.dife.api.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController implements SwaggerPostController {

	private final PostService postService;

	@GetMapping
	public ResponseEntity<List<PostResponseDto>> getPostsByBoardType(BoardCategory boardCategory) {
		List<PostResponseDto> responseDto = postService.getPostsByBoardType(boardCategory);
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<PostResponseDto> createPost(
			@RequestBody PostCreateRequestDto requestDto, Authentication auth) {

		PostResponseDto responseDto = postService.createPost(requestDto, auth.getName());

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDto> getPost(@PathVariable(name = "id") Long id) {
		PostResponseDto responseDto = postService.getPost(id);
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<PostResponseDto> updatePost(
			@PathVariable(name = "id") Long id,
			@RequestBody PostUpdateRequestDto requestDto,
			Authentication auth) {
		PostResponseDto responseDto = postService.updatePost(id, requestDto, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id, Authentication auth) {
		postService.deletePost(id, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
