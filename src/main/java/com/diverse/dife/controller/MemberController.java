package com.diverse.dife.controller;


import com.diverse.dife.entity.Member;
import com.diverse.dife.repository.MemberRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;

    @PostMapping("/save")
    public void memberSave(@RequestBody Member member)
    {
        memberRepository.save(member);
    }
}
