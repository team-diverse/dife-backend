package com.dife.api.controller;


import com.dife.api.model.Member;
import com.dife.api.model.dto.MemberDto;
import com.dife.api.model.dto.RegisterRequestDto;
import com.dife.api.model.dto.VerifyEmailDto;
import com.dife.api.repository.MemberRepository;
import com.dife.api.service.MemberService;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

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
    public ResponseEntity<MemberDto> register(@Valid @RequestBody RegisterRequestDto request, UriComponentsBuilder uriBuilder) {
        Member member = memberService.register(request);

        MemberDto memberDto = new MemberDto(member);
        URI location = uriBuilder.path("/api/members/mypage").buildAndExpand(member.getId()).toUri();

        return ResponseEntity.created(location).body(memberDto);

    }

    @GetMapping("/mypage")
    public ResponseEntity<MemberDto> profile()
    {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.getMember(memberEmail);
        log.info("TokenID : " + member.getTokenId());
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
