package com.dife.member.service;

import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberUpdateDto;
import com.dife.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public Member updateMember(MemberUpdateDto memberUpdateDto)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberUpdateDto.getEmail());

        if (optionalMember.isEmpty())
        {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        Member member = optionalMember.get();

        member.setEmail(memberUpdateDto.getEmail());
        member.setPassword(memberUpdateDto.getPassword());
        member.setIs_korean(memberUpdateDto.getIs_korean());
        member.setBio(memberUpdateDto.getBio());
        member.setMbti(memberUpdateDto.getMbti());
        member.setIs_public(memberUpdateDto.getIs_public());
        member.setFile_id(memberUpdateDto.getFile_id());
        member.setNickname(memberUpdateDto.getNickname());

        memberRepository.save(member);

        return member;
    }
}
