package com.dife.member.service;

import com.dife.member.exception.DuplicateMemberException;
import com.dife.member.exception.UnAuthorizationException;
import com.dife.member.jwt.JWTUtil;
import com.dife.member.model.Member;
import com.dife.member.model.dto.MemberDto;
import com.dife.member.model.dto.VerifyEmailDto;
import com.dife.member.repository.MemberRepository;
import com.dife.member.model.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final JavaMailSender javaMailSender;

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
        member.setPassword(memberUpdateDto.getPassword());
        member.setIs_korean(memberUpdateDto.getIs_korean());
        member.setBio(memberUpdateDto.getBio());
        member.setMbti(memberUpdateDto.getMbti());
        member.setIs_public(memberUpdateDto.getIs_public());
        member.setNickname(memberUpdateDto.getNickname());

        memberRepository.save(member);
    }

    public boolean changePassword(VerifyEmailDto emailDto)
    {
        Optional<Member> optionalMember = memberRepository.findByEmail(emailDto.getEmail());
        if (optionalMember.isEmpty())
        {
            return false;
        }

        Member member = optionalMember.get();

        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(charset.charAt(random.nextInt(charset.length())));
        }

        String newPassword = sb.toString();
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);

        memberRepository.save(member);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(member.getEmail());
        simpleMailMessage.setSubject("🤿 DIFE 비밀번호 변경 메일 🤿");
        simpleMailMessage.setText("비밀번호를 잊으셨나요? 🥹\n" +
                "걱정하지 마세요!. 새 비밀번호를 부여해드릴게요!\n" +
                "새 비밀번호 : " + newPassword + "\n" +
                "안전한 인터넷 환경에서 항상 비밀번호를 관리하세요.");
        javaMailSender.send(simpleMailMessage);
        return true;
    }


}
