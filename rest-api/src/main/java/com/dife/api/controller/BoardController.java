package com.dife.api.controller;

import com.dife.api.model.BOARD_category;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import com.dife.api.model.dto.BoardDto;
import com.dife.api.model.dto.PostCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import com.dife.api.service.MemberService;
import com.dife.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final MemberService memberService;
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostCreateRequestDto request, Authentication auth) {
        Member currentMember = memberService.getMember(auth.getName());
        Post post = postService.create(request, currentMember);

        return ResponseEntity
                .status(CREATED.value())
                .body(new PostResponseDto(post));
    }

    @GetMapping("/{boardType}/posts")
    public ResponseEntity<List<BoardDto>> getPostsByBoardType(@PathVariable("boardType") BOARD_category boardType) {
        List<BoardDto> posts = postService.getPostsByBoardType(boardType);
        return ResponseEntity.ok(posts);
    }

}
