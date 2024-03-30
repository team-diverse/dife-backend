package com.dife.member.controller;


import com.dife.member.jwt.JWTUtil;
import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberUpdateDto;
import com.dife.member.model.dto.RegisterRequestDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Slf4j
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto request) {
        this.memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("유저가 생성되었습니다.");
    }

    @GetMapping("/mypage")
    public ResponseEntity<String> profile()
    {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.getMember(memberEmail);
        return ResponseEntity.status(HttpStatus.OK).body(member.getEmail() + "유저의 마이페이지 입니다.\n유저 소개말 : " + member.getBio());
    }

    @PutMapping("/mypage")
    public ResponseEntity<String> editProfile(@RequestBody MemberUpdateDto memberUpdateDto)
    {
        Member member = memberService.updateMember(memberUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(member.getEmail() + "유저 업데이트된 마이페이지입니다.\n유저 소개말 : " + member.getBio());
    }

}
