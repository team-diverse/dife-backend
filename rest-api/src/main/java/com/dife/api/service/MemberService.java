package com.dife.api.service;

import com.dife.api.model.Member;
import com.dife.api.model.dto.MemberUpdateDto;
import com.dife.api.repository.MemberRepository;
import com.dife.api.model.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
        if (optionalMember.isEmpty())
        {
            throw new UnAuthorizationException("인증되지 않은 회원입니다!");
        }
        Member member = optionalMember.get();
        return member;
    }

    public void updateMember(Member member, MemberDto memberUpdateDto)
    {
        Optional<Member> optionalMember = memberRepository.findById(id);

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
    }
}
