package com.dife.member.service;

import com.dife.member.exception.MemberNotFoundException;
import com.dife.member.model.dto.CustomUserDetails;
import com.dife.member.model.Member;
import com.dife.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty())
        {
            return null;
        }

        Member member = optionalMember.get();
        return new CustomUserDetails(member);
    }
}
