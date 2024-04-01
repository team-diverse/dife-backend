package com.dife.member.controller;

import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberDto;
import com.dife.member.model.dto.RegisterRequestDto;
import com.dife.member.model.dto.RegisterResponseDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        Pair<RegisterResponseDto, String> pair = memberService.register(request);

        RegisterResponseDto responseDto = pair.getLeft();
        String token = pair.getRight();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity
                .status(CREATED.value())
                .headers(headers)
                .body(responseDto);
    }

    @GetMapping("/mypage")
    public ResponseEntity<MemberDto> profile()
    {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.getMember(memberEmail);
        log.info(member.getTokenId());
        MemberDto memberDto = new MemberDto(member);
        return ResponseEntity.ok(memberDto);
    }

    @PutMapping("/mypage")
    public ResponseEntity<MemberDto> editProfile(@RequestBody MemberDto memberUpdateDto)
    {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.getMember(memberEmail);
        this.memberService.updateMember(member, memberUpdateDto);

        MemberDto memberDto = new MemberDto(member);
        return ResponseEntity.ok(memberDto);
    }

}
