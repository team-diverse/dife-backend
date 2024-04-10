package com.dife.api.controller;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class BoardController {

    private final MemberService memberService;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostCreateRequestDto request, Authentication auth) {
        Member currentMember = memberService.getMember(auth.getName());
        Post post = postService.create(request, currentMember);

        return ResponseEntity
                .status(CREATED.value())
                .body(new PostResponseDto(post));
    }

    @GetMapping("/")
    public ResponseEntity<List<BoardDto>> getPostsByBoardType(@RequestParam("boardType") BoardCategory boardType) {
        List<BoardDto> posts = postService.getPostsByBoardType(boardType);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> seatchById(@PathVariable Long id)
    {
        Post post = postService.getPost(id);
        return ResponseEntity
                .status(OK.value())
                .body(new PostResponseDto(post));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> editPost(@PathVariable Long id, @RequestBody PostUpdateRequestDto request, Authentication auth)
    {
        Member currentMember = memberService.getMember(auth.getName());
        Post post = postService.updatePost(id, request, currentMember);

        return ResponseEntity
                .status(OK.value())
                .body(new PostResponseDto(post));
    }
}
