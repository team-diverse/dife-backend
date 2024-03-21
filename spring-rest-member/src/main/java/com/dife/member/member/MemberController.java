package com.dife.member.member;


import com.dife.member.model.Member;
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

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
@Slf4j
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    private final MemberService memberService;

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
    }
}
