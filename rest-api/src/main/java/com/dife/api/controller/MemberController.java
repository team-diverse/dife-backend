package com.dife.api.controller;


import com.dife.api.model.Member;
import com.dife.api.model.dto.MemberDto;
import com.dife.api.model.dto.MemberUpdateDto;
import com.dife.api.model.dto.RegisterRequestDto;
import com.dife.api.model.dto.VerifyEmailDto;
import com.dife.api.repository.MemberRepository;
import com.dife.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterRequestDto> register(@Valid @RequestBody RegisterRequestDto request) {
        this.memberService.register(request);
        return ResponseEntity
                .status(CREATED.value())
                .body(new RegisterRequestDto(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberDto> profile()
    {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.getMember(memberEmail);
        log.info(member.getTokenId());
        MemberDto memberDto = new MemberDto(member);
        return ResponseEntity.ok(memberDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<MemberUpdateDto> editProfile(@RequestBody MemberUpdateDto requestDto, Authentication auth)
    {
        String email = auth.getName();
        Member member = this.memberService.updateMember(email, requestDto);

        MemberUpdateDto responseDto = modelMapper.map(member, MemberUpdateDto.class);
        return ResponseEntity.ok(responseDto);
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
