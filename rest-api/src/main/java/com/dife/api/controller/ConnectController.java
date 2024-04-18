package com.dife.api.controller;

import com.dife.api.model.Member;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.service.ConnectService;
import com.dife.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connects")
public class ConnectController {

    private final ConnectService connectService;
    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity<String> connect(@Valid @RequestBody ConnectRequestDto requestDto, Authentication auth) {
        Member currentMember = memberService.getMember(auth.getName());
        connectService.connectMembers(requestDto, currentMember);

        return ResponseEntity.status(HttpStatus.CREATED).body("커넥트 추가 성공");
    }
}
