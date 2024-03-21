package com.dife.member.controller;


import com.dife.member.service.MemberService;
import com.dife.member.model.dto.LoginDto;
import com.dife.member.model.dto.RegisterRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto request) {
        this.memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("유저가 생성되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto)
    {
        this.memberService.login(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("유저가 로그인했습니다.");
    }

}
