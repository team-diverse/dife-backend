package com.dife.member.controller;


import com.dife.member.model.Member;
import com.dife.member.model.dto.*;

import com.dife.member.model.dto.RegisterRequestDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Slf4j
public class MemberController {

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
        log.info("TokenId : " + member.getTokenId());
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


    @PutMapping("/change-password")
    public ResponseEntity<HashMap> mailCheck(@RequestBody VerifyEmailDto emailDto)
    {
        boolean success = memberService.changePassword(emailDto);

        HashMap<String, Object> responseMap = new HashMap<>();

        if (success) {
            responseMap.put("status", 200);
            responseMap.put("message", "메일 발송 성공");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
        else
        {
            responseMap.put("status", 500);
            responseMap.put("message", "메일 발송 실패");
            return new ResponseEntity<HashMap> (responseMap, HttpStatus.CONFLICT);
        }

    }

}
