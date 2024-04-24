package com.dife.api.controller;

import com.dife.api.model.Member;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectResponseDto;
import com.dife.api.service.ConnectService;
import com.dife.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connects")
public class ConnectController {

    private final ConnectService connectService;
    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity<ConnectResponseDto> createConnect(@Valid @RequestBody ConnectRequestDto requestDto, Authentication auth) {
        ConnectResponseDto responseDto = connectService.saveConnect(requestDto, auth.getName());
        return ResponseEntity.status(CREATED).body(responseDto);
    }
}
