package com.dife.member.service;

import com.dife.member.model.Member;
import com.dife.member.repository.MemberRepository;
import dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(RegisterRequestDto dto) {
        Member member = modelMapper.map(dto, Member.class);

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPassword);

        memberRepository.save(member);
    }
}
