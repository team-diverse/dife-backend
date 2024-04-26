package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.dto.BoardDto;
import com.dife.api.model.dto.PostCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import com.dife.api.model.dto.PostUpdateRequestDto;
import com.dife.api.service.MemberService;
import com.dife.api.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class BoardController {

	private final MemberService memberService;
	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostResponseDto> create(
			@Valid @RequestBody PostCreateRequestDto request, Authentication auth) {
		Member currentMember = memberService.getMember(auth.getName());
		Post post = postService.create(request, currentMember);

		return ResponseEntity.status(CREATED.value()).body(new PostResponseDto(post));
	}

	@GetMapping("/")
	public ResponseEntity<List<BoardDto>> getPostsByBoardType(
			@RequestParam("boardType") BoardCategory boardType) {
		List<BoardDto> posts = postService.getPostsByBoardType(boardType);
		return ResponseEntity.ok(posts);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
		Post post = postService.getPost(id);
		return ResponseEntity.status(OK).body(new PostResponseDto(post));
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostResponseDto> updatePost(
			@PathVariable Long id, @RequestBody PostUpdateRequestDto request, Authentication auth) {
		Member currentMember = memberService.getMember(auth.getName());
		Post post = postService.updatePost(id, request, currentMember);

		return ResponseEntity.status(OK).body(new PostResponseDto(post));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable Long id, Authentication auth) {
		Member currentMember = memberService.getMember(auth.getName());
		this.postService.deletePost(id, currentMember);
		return ResponseEntity.ok().body("게시물이 삭제되었습니다!");
	}
}
