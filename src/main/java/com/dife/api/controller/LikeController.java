package com.dife.api.controller;

import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

	private final LikeService likeService;

	@PostMapping
	public ResponseEntity<Void> createLike(
			@RequestBody LikeCreateRequestDto requestDto, Authentication auth) {

		likeService.createLike(requestDto, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
