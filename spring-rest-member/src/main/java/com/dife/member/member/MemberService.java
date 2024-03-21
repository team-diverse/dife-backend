package com.dife.member.member;

import com.dife.member.model.MBTI_category;
import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public Long editMemberProfile(MemberDto memberDto)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberDto.getEmail());

        if (optionalMember.isEmpty())
        {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        Member member = optionalMember.get();


        member.editUsername(member.getUsername());
        member.editBio(member.getBio());
        member.editFile_id(member.getFile_id());
        member.editMbti(String.valueOf(member.getMbti()));
        member.editIs_public(member.getIs_public());


        member.editPassword(member.getPassword());
        memberRepository.save(member);

        return member.getId();
    }
}
