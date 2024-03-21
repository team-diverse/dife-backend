package com.dife.member.member;


import com.dife.member.model.Member;
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

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
<<<<<<< HEAD
@RequestMapping("/api/members")
=======
@RequestMapping("api/members")
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
@Slf4j
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    private final MemberService memberService;

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
    {
        String loginId = principal.getName();
        Optional<Member> optionalMember = memberRepository.findByEmail(loginId);
        Member member = optionalMember.get();
        modelMap.addAttribute("member", member);
        return "/profile";
    }
    @PostMapping("/profile/edit")
    public String editProfile(@Valid MemberDto memberDto, Model model)
    {
        model.addAttribute("member", memberDto);
        memberService.editMemberProfile(memberDto);
        return "redirect:/member/profile";
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
    }
}
