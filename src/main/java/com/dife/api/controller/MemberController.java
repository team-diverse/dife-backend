package com.dife.api.controller;


import com.dife.api.model.MbtiCategory;
import com.dife.api.model.Member;
import com.dife.api.model.dto.*;
import com.dife.api.model.dto.RegisterDto.*;
import com.dife.api.service.FileService;
import com.dife.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final FileService fileService;

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register1(@Valid @RequestBody Register1RequestDto dto) {
        Member member = memberService.register1(dto);

        return ResponseEntity
                .status(CREATED.value())
                .body(new MemberResponseDto(member));
    }

    @GetMapping("/register")
    public ResponseEntity<String> registerStep2(@RequestParam("username") String username) {
        Boolean result = memberService.register2(username);

        if (result)
        {
            return ResponseEntity.status(HttpStatus.OK).body("유효한 닉네임입니다!");
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("유효하지 않은 닉네임입니다!");
    }

    @PutMapping("/register/{id}")
    public ResponseEntity<MemberResponseDto> registerStep7(@RequestParam("username") String username,
                                                           @RequestParam(value = "is_korean", required = false) Boolean is_korean,
                                                           @RequestParam(value = "bio", required = false) String bio,
                                                           @RequestParam("mbti") MbtiCategory mbti,
                                                           @RequestParam("hobbies") Set<String> hobbies,
                                                           @RequestParam(value = "languages", required = false) Set<String> languages,
                                                           @RequestParam(value = "profile_img", required = false) MultipartFile profile_img,
                                                           @RequestParam("verification_file") MultipartFile verification_file,
                                                           @PathVariable Long id) {

        FileDto profileImgDto = fileService.upload(profile_img);
        FileDto verificationFileDto = fileService.upload(verification_file);

        ModelMapper modelMapper = new ModelMapper();
        Register7RequestDto dto = modelMapper.map(new Register7RequestDto(username, is_korean, bio, mbti, hobbies, languages), Register7RequestDto.class);

        Member member = memberService.register7(dto, id);
        member.setProfile_file_id(profileImgDto.getUrl());
        member.setVerification_file_id(verificationFileDto.getUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponseDto(member));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponseDto> profile()
    {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.getMember(memberEmail);
        log.info("TokenId : " + member.getTokenId());
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);
        return ResponseEntity.ok(memberResponseDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<MemberResponseDto> editProfile(@RequestBody MemberUpdateDto dto)
    {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.updateMember(memberEmail, dto);

        MemberResponseDto memberResponseDto = new MemberResponseDto(member);
        return ResponseEntity.ok(memberResponseDto);
    }

    @PutMapping("/change-password")
    public ResponseEntity<HashMap> mailCheck(@RequestBody VerifyEmailDto emailDto)
    {
        boolean success = memberService.changePassword(emailDto);

        HashMap<String, Object> responseMap = new HashMap<>();

        if (success) {
            responseMap.put("status", 200);
            responseMap.put("message", "메일 발송 성공");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        }
        else
        {
            responseMap.put("status", 500);
            responseMap.put("message", "메일 발송 실패");
            return new ResponseEntity<HashMap> (responseMap, HttpStatus.CONFLICT);
        }

    }

}
