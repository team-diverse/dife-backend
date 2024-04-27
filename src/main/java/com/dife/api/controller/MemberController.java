package com.dife.api.controller;


import com.dife.api.model.MbtiCategory;
import com.dife.api.model.Member;
import com.dife.api.model.dto.*;
import com.dife.api.service.FileService;
import com.dife.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<MemberResponseDto> registerEmailAndPassword(@Valid @RequestBody RegisterEmailAndPasswordRequestDto dto) {
        Member member = memberService.registerEmailAndPassword(dto);

        return ResponseEntity
                .status(CREATED.value())
                .body(new MemberResponseDto(member));
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkUsername(@RequestParam("username") String username, @PathVariable Long id) {
        Boolean isValid = memberService.checkUsername(username);

        if (isValid)
        {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping( "/{id}")
    public ResponseEntity<MemberResponseDto> registerDetail(@RequestParam("username") String username,
                                                           @RequestParam("is_korean") Boolean is_korean,
                                                           @RequestParam("bio") String bio,
                                                           @RequestParam("mbti") MbtiCategory mbti,
                                                           @RequestParam("hobbies") Set<String> hobbies,
                                                           @RequestParam("languages") Set<String> languages,
                                                           @RequestParam("profile_img") MultipartFile profile_img,
                                                           @RequestParam("verification_file") MultipartFile verification_file,
                                                           @PathVariable Long id) {

        Member member = memberService.registerDetail(username, is_korean, bio, mbti, hobbies, languages, id, profile_img, verification_file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponseDto(member));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponseDto> profile(Authentication auth)
    {
        Member currentMember = memberService.getMember(auth.getName());
        log.info("TokenId : " + currentMember.getTokenId());
        MemberResponseDto memberResponseDto = new MemberResponseDto(currentMember);
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
