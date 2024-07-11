package com.dife.api.controller;

import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import com.dife.api.service.LikeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController implements SwaggerLikeController {

	private final LikeService likeService;

	@GetMapping
	public ResponseEntity<List<PostResponseDto>> getLikedPosts(Authentication auth) {
		List<PostResponseDto> responseDto = likeService.getLikedPosts(auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping
	public ResponseEntity<Void> createLike(
			@RequestBody LikeCreateRequestDto requestDto, Authentication auth) {

		likeService.createLike(requestDto, auth.getName());
		return new ResponseEntity<>(OK);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteLikePost(
			@RequestBody LikeCreateRequestDto requestDto, Authentication auth) {
		likeService.deleteLikePost(requestDto, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
