package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.Member;
import com.dife.api.model.dto.*;
import com.dife.api.service.MemberService;
import com.dife.api.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class BoardController implements SwaggerBoardController {

	private final MemberService memberService;
	private final PostService postService;

	@GetMapping("/")
	public ResponseEntity<List<PostResponseDto>> getPostsByBoardType(BoardRequestDto requestDto) {
		List<PostResponseDto> responseDto = postService.getPostsByBoardType(requestDto);
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping
	public ResponseEntity<PostResponseDto> create(
			PostCreateRequestDto requestDto, Authentication auth) {

		Member currentMember = memberService.getMember(auth.getName());
		PostResponseDto responseDto = postService.create(requestDto, currentMember);

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDto> getPost(@PathVariable(name = "id") Long id) {
		PostResponseDto responseDto = postService.getPost(id);
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostResponseDto> updatePost(
			@PathVariable(name = "id") Long id, PostUpdateRequestDto requestDto, Authentication auth) {
		Member currentMember = memberService.getMember(auth.getName());
		PostResponseDto responseDto = postService.updatePost(id, requestDto, currentMember);
		return ResponseEntity.status(OK).body(responseDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id, Authentication auth) {
		Member currentMember = memberService.getMember(auth.getName());
		postService.deletePost(id, currentMember);
		return new ResponseEntity<>(OK);
	}
}
