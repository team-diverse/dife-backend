package com.dife.api.controller;


import com.dife.api.model.MbtiCategory;
import com.dife.api.model.Member;
import com.dife.api.model.dto.*;
import com.dife.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Set;


@Tag(name = "Member API", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @Operation(summary = "회원가입1 API", description = "이메일과 비밀번호를 사용하여 새 회원을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입1 성공 예시", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponseDto.class))})
    public ResponseEntity<RegisterResponseDto> registerEmailAndPassword(
            @RequestBody(description = "이메일과 비밀번호를 포함하는 등록 데이터", required = true, content = @Content(schema = @Schema(implementation = RegisterEmailAndPasswordRequestDto.class))) RegisterEmailAndPasswordRequestDto dto) {
        Member member = memberService.registerEmailAndPassword(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponseDto(member));
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
    @Operation(summary = "회원가입2 API", description = "회원가입 세부사항을 입력합니다.")
    @ApiResponse(responseCode = "201", description = "회원가입2 성공 예시", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDto.class))})
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
