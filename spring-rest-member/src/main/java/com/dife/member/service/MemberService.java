package com.dife.member.service;

import com.dife.member.exception.DuplicateMemberException;
import com.dife.member.exception.MemberNotFoundException;
import com.dife.member.jwt.JWTUtil;
import com.dife.member.model.Member;
import com.dife.member.model.dto.LoginDto;
import com.dife.member.model.dto.MemberUpdateDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.model.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public void register(RegisterRequestDto dto) {
        Member member = modelMapper.map(dto, Member.class);

        if (memberRepository.existsByEmail(dto.getEmail()))
        {
            throw new DuplicateMemberException("이미 등록한 회원입니다!");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPassword);

        memberRepository.save(member);
    }

    public Member getMember(String email) {

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.get();

        return member;
    }

    public Member updateMember(MemberUpdateDto memberUpdateDto)
    {
        String email = memberUpdateDto.getEmail();
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty())
        {
            throw new MemberNotFoundException("존재하지 않는 회원입니다.");
        }

        Member member = optionalMember.get();

        member.setPassword(memberUpdateDto.getPassword());
        member.setIs_korean(memberUpdateDto.getIs_korean());
        member.setBio(memberUpdateDto.getBio());
        member.setMbti(memberUpdateDto.getMbti());
        member.setIs_public(memberUpdateDto.getIs_public());
        member.setNickname(memberUpdateDto.getNickname());

        memberRepository.save(member);

        return member;
    }
}
