package com.dife.member.controller;


import com.dife.member.model.Member;
import com.dife.member.model.dto.LoginDto;
import com.dife.member.model.dto.MemberUpdateDto;
import com.dife.member.model.RegisterRequestDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
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

    @GetMapping("/{id}")
    public String profile(@PathVariable Long id)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember.get();
        return "사용자 정보 조회";
    }
    @PutMapping("/{id}")
    public String editProfile(@PathVariable Long id, MemberUpdateDto memberUpdateDto)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember.get();
        member = memberService.updateMember(memberUpdateDto);
        return "사용자 정보 수정";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto request) {
        this.memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("유저가 생성되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto request) {
        Member member = memberService.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(member.getEmail() + "유저가 로그인했습니다.");
    }

}
