package com.dife.member.service;

import com.dife.member.jwt.JWTUtil;
import com.dife.member.model.Member;
import com.dife.member.model.dto.LoginDto;
import com.dife.member.model.dto.MemberUpdateDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.model.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPassword);

        memberRepository.save(member);
    }

    public String login(LoginDto dto)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());

        if (optionalMember.isEmpty())
        {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        Member member = optionalMember.get();
        return jwtUtil.createJwt(member.getEmail(), member.getRole(), 3000L);
    }

    @Transactional
    public Member updateMember(Long id, MemberUpdateDto memberUpdateDto)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isEmpty())
        {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
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
