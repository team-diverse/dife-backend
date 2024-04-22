package com.dife.api.service;


import com.dife.api.config.EmailValidator;
import com.dife.api.exception.*;
import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.Hobby;
import com.dife.api.model.Image;
import com.dife.api.model.Language;
import com.dife.api.model.dto.*;
import com.dife.api.model.Member;
import com.dife.api.model.dto.RegisterDto.*;
import com.dife.api.repository.HobbyRepository;
import com.dife.api.repository.ImageRepository;
import com.dife.api.repository.LanguageRepository;
import com.dife.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Slf4j
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    private final LanguageRepository languageRepository;
    private final HobbyRepository hobbyRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final JWTUtil jwtUtil;
    private final JavaMailSender javaMailSender;
    private final FileService fileService;
    private final ImageRepository imageRepository;

    public Member register1(Register1RequestDto dto)
    {
        if(!emailValidator.isValidEmail(dto.getEmail())) {
            throw new RegisterException("유효하지 않은 이메일입니다");
        }

        if (dto.getPassword() == null || dto.getPassword().length() < 8 || !dto.getPassword().matches("^[a-zA-Z0-9]{8,}$")) {
            throw new RegisterException("영문, 숫자 포함 8자 이상의 비밀번호를 입력해주세요");
        }

        if (memberRepository.existsByEmail(dto.getEmail()))
        {
            throw new DuplicateMemberException("이미 가입되어있는 이메일입니다");
        }

        Member member = new Member();
        member.setEmail(dto.getEmail());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPassword);

        memberRepository.save(member);
        return member;

    }
    public Boolean register2(String username)
    {
        if (memberRepository.existsByUsername(username))
        {
            return false;
        }
        return true;
    }

    public Member register7(Register7RequestDto dto, Long id)
    {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다!"));

        member.setUsername(dto.getUsername());
        member.setIs_korean(dto.getIs_korean());
        member.setBio(dto.getBio());
        member.setMbti(dto.getMbti());

        Set<Hobby> hobbies = new HashSet<>();
        for (String hob : dto.getHobbies()) {
            Hobby hobby = new Hobby();
            hobby.setName(hob);
            hobby.setMember(member);

            hobbies.add(hobby);
            hobbyRepository.save(hobby);
        }
        member.setHobbies(hobbies);

        Set<Language> languages = new HashSet<>();
        for (String lan : dto.getLanguages()) {
            Language language = new Language();
            language.setName(lan);
            language.setMember(member);

            languages.add(language);
            languageRepository.save(language);
        }
        member.setLanguages(languages);

        memberRepository.save(member);

        return member;
    }



    public Member getMember(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다!"));

        return member;

    }

    public Member updateMember(String email, MemberUpdateDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다!"));

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPassword);
        member.setUsername(dto.getUsername());
        member.setIs_public(dto.getIs_public());

        Set<Language> languages = new HashSet<>();

        for (String lan : dto.getLanguages())
        {
            Optional<Language> languageOptional = languageRepository.findByMemberAndName(member, lan);
            if (!languageOptional.isPresent()) {
                Language newLanguage = new Language();
                newLanguage.setName(lan);
                newLanguage.setMember(member);
                languageRepository.save(newLanguage);
                languages.add(newLanguage);
            } else {
                languages.add(languageOptional.get());
            }
        }

        member.setLanguages(languages);

        Set<Hobby> hobbies = new HashSet<>();

        for (String hob : dto.getHobbies())
        {
            Optional<Hobby> hobbyOptional = hobbyRepository.findByMemberAndName(member, hob);
            if (!hobbyOptional.isPresent()) {
                Hobby newHobby = new Hobby();
                newHobby.setName(hob);
                newHobby.setMember(member);
                hobbyRepository.save(newHobby);
                hobbies.add(newHobby);
            } else {
                hobbies.add(hobbyOptional.get());
            }
        }

        member.setHobbies(hobbies);

        member.setProfile_file_id(dto.getProfile_file_id());
        member.setBio(dto.getBio());

        memberRepository.save(member);

        return member;
    }

    public boolean changePassword(VerifyEmailDto emailDto)
    {

        if (!memberRepository.existsByEmail(emailDto.getEmail()))
        {
            return false;
        }
        Optional<Member> optionalMember = memberRepository.findByEmail(emailDto.getEmail());
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
