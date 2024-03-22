package com.dife.member.member;


import com.dife.member.model.Member;
<<<<<<< HEAD
<<<<<<< HEAD
import com.dife.member.model.dto.EditProfileDto;
import com.dife.member.model.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
=======
import com.dife.member.model.dto.MemberDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
=======
import com.dife.member.model.dto.EditProfileDto;
import com.dife.member.model.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)

import java.util.Optional;

@RestController
@RequiredArgsConstructor
<<<<<<< HEAD
<<<<<<< HEAD
@RequestMapping("/api/members")
=======
@RequestMapping("api/members")
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
=======
@RequestMapping("/api/members")
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)
@Slf4j
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    private final MemberService memberService;

<<<<<<< HEAD
<<<<<<< HEAD
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

=======
    @PostMapping("/login")
    public String profile(Principal principal, ModelMap modelMap)
=======
    @GetMapping("/{id}")
    public String profile(@PathVariable Long id)
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember.get();
        return "사용자 정보 조회";
    }
    @PutMapping("/{id}")
    public String editProfile(@PathVariable Long id, MemberUpdateDto memberUpdateDto)
    {
<<<<<<< HEAD
        model.addAttribute("member", memberDto);
        memberService.editMemberProfile(memberDto);
        return "redirect:/member/profile";
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
=======
        Optional<Member> optionalMember = memberRepository.findById(id);
        Member member = optionalMember.get();
        member = memberService.updateMember(memberUpdateDto);
        return "사용자 정보 수정";

>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)
    }
}
