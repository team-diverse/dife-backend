package com.dife.member.service;

import com.dife.member.exception.DuplicateMemberException;
import com.dife.member.exception.UnAuthorizationException;
import com.dife.member.jwt.JWTUtil;
import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberDto;
import com.dife.member.model.dto.RegisterResponseDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.model.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public Pair<RegisterResponseDto, String> register(RegisterRequestDto requestDto) {
        Member member = modelMapper.map(requestDto, Member.class);

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateMemberException("이미 등록한 회원입니다!");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        member.setPassword(encodedPassword);
        memberRepository.save(member);

        RegisterResponseDto responseDto = modelMapper.map(member, RegisterResponseDto.class);
        String token = jwtUtil.createAccessJwt(member.getEmail(), member.getRole(), 60 * 60 * 1000L);

        return Pair.of(responseDto, token);
    }

    public Member getMember(String email) {

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty())
        {
            throw new UnAuthorizationException("인증되지 않은 회원입니다!");
        }
        Member member = optionalMember.get();
        return member;
    }

    public void updateMember(Member member, MemberDto memberUpdateDto)
    {
        member.setPassword(memberUpdateDto.getPassword());
        member.setIs_korean(memberUpdateDto.getIs_korean());
        member.setBio(memberUpdateDto.getBio());
        member.setMbti(memberUpdateDto.getMbti());
        member.setIs_public(memberUpdateDto.getIs_public());
        member.setNickname(memberUpdateDto.getNickname());

        memberRepository.save(member);
    }
}
