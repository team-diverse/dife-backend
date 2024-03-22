package com.dife.member.member;

<<<<<<< HEAD
<<<<<<< HEAD
import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberUpdateDto;
=======
import com.dife.member.model.MBTI_category;
import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberDto;
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
=======
import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberUpdateDto;
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)
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
<<<<<<< HEAD
<<<<<<< HEAD
    public Member updateMember(MemberUpdateDto memberUpdateDto)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberUpdateDto.getEmail());
=======
    public Long editMemberProfile(MemberDto memberDto)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberDto.getEmail());
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
=======
    public Member updateMember(MemberUpdateDto memberUpdateDto)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberUpdateDto.getEmail());
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)

        if (optionalMember.isEmpty())
        {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        Member member = optionalMember.get();

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)
        member.setEmail(memberUpdateDto.getEmail());
        member.setPassword(memberUpdateDto.getPassword());
        member.setIs_korean(memberUpdateDto.getIs_korean());
        member.setBio(memberUpdateDto.getBio());
        member.setMbti(memberUpdateDto.getMbti());
        member.setIs_public(memberUpdateDto.getIs_public());
        member.setFile_id(memberUpdateDto.getFile_id());
        member.setNickname(memberUpdateDto.getNickname());
<<<<<<< HEAD

        memberRepository.save(member);

        return member;
=======
=======
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)

        memberRepository.save(member);

<<<<<<< HEAD
        return member.getId();
>>>>>>> 89e91e0 (마이페이지(profile) 편집 기능 추가)
=======
        return member;
>>>>>>> 9f3177a (refactor: 마이페이지 수정 기능 보완)
    }
}
