package com.dife.api.controller;


import com.dife.api.model.Member;
import com.dife.api.model.dto.MemberDto;
import com.dife.api.model.dto.RegisterRequestDto;
import com.dife.api.repository.MemberRepository;
import com.dife.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MemberRepository memberRepository;
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<RegisterRequestDto> register(@Valid @RequestBody RegisterRequestDto request) {
        this.memberService.register(request);
        return ResponseEntity
                .status(CREATED.value())
                .body(new RegisterRequestDto(request));
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
