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
import org.springframework.web.multipart.MultipartFile;

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

	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<PostResponseDto> createPost(
			@RequestParam(name = "tokenId") Long tokenId,
			@RequestParam(name = "title") String title,
			@RequestParam(name = "content") String content,
			@RequestParam(name = "isPublic") Boolean isPublic,
			@RequestParam(name = "boardType") BoardCategory boardType,
			@RequestParam(name = "postFile", required = false) MultipartFile postFile,
			Authentication auth) {

		PostResponseDto responseDto =
				postService.createPost(
						tokenId, title, content, isPublic, boardType, postFile, auth.getName());

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDto> getPost(
			@PathVariable(name = "id") Long id, Authentication auth) {
		PostResponseDto responseDto = postService.getPost(id, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<PostResponseDto> updatePost(
			@PathVariable(name = "id") Long id,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "content", required = false) String content,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			@RequestParam(name = "boardType", required = false) BoardCategory boardType,
			@RequestParam(name = "postFile", required = false) MultipartFile postFile,
			Authentication auth) {
		PostResponseDto responseDto =
				postService.updatePost(id, title, content, isPublic, boardType, postFile, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id, Authentication auth) {
		postService.deletePost(id, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
