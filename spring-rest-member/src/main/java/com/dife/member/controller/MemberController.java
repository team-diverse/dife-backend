package com.dife.member.controller;


import com.dife.member.model.Member;
import com.dife.member.model.dto.EditProfileDto;
import com.dife.member.model.dto.MemberUpdateDto;
import com.dife.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
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
}
