package com.dife.member.controller;


<<<<<<< HEAD
=======
import com.dife.member.exception.MemberNotFoundException;
import com.dife.member.jwt.JWTUtil;
>>>>>>> c3768c7 (에러 헨들링 코드 작성)
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
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto request) {
        this.memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("유저가 생성되었습니다.");
    }
<<<<<<< HEAD

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto request) {
        String tokenId = memberService.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("토큰ID : " + tokenId);
=======
    @GetMapping("/mypage")
    public ResponseEntity<String> profile()
    {
        try
        {
            String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Member member = memberService.getMember(memberEmail);
            return ResponseEntity.status(HttpStatus.OK).body(member.getEmail() + "유저의 마이페이지 입니다.\n유저 소개말 :" + member.getBio());
        }
        catch (MemberNotFoundException e)
        {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다!");
        }

>>>>>>> c3768c7 (에러 헨들링 코드 작성)
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> profile(@PathVariable Long id)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember.get();
        String confirm = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK).body(member.getEmail() + "유저 마이페이지입니다.\n유저 소개말 : " + member.getBio()
                                                            + "\n현재 세션 아이디 : " + confirm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editProfile(@PathVariable Long id, @RequestBody MemberUpdateDto memberUpdateDto)
    {
        Member member = memberService.updateMember(id, memberUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(member.getEmail() + "유저 업데이트된 마이페이지입니다.\n유저 소개말 : " + member.getBio());
    }

}
