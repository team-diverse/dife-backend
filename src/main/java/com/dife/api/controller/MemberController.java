package com.dife.api.controller;

import com.dife.api.model.MbtiCategory;
import com.dife.api.model.Member;
import com.dife.api.model.dto.*;
import com.dife.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member API", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/register")
	@Operation(summary = "회원가입1 API", description = "이메일과 비밀번호를 사용하여 새 회원을 등록합니다.")
	@ApiResponse(
			responseCode = "201",
			description = "회원가입1 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = RegisterResponseDto.class))
			})
	public ResponseEntity<RegisterResponseDto> registerEmailAndPassword(
			@RequestBody(
							description = "이메일과 비밀번호를 포함하는 등록 데이터",
							required = true,
							content =
									@Content(
											schema = @Schema(implementation = RegisterEmailAndPasswordRequestDto.class)))
					RegisterEmailAndPasswordRequestDto dto) {
		Member member = memberService.registerEmailAndPassword(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponseDto(member));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.HEAD)
	@Operation(summary = "중복 닉네임 확인", description = "중복 닉네임 여부를 확인합니다.")
	public ResponseEntity<Void> checkUsername(
			@RequestParam(name = "username") String username, @PathVariable(name = "id") Long id) {
		Boolean isValid = memberService.checkUsername(username);

		if (isValid) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	@Operation(summary = "회원가입2 API", description = "회원가입 세부사항을 입력합니다.")
	@ApiResponse(
			responseCode = "201",
			description = "회원가입2 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = MemberResponseDto.class))
			})
	public ResponseEntity<MemberResponseDto> registerDetail(
			@RequestParam(name = "username") String username,
			@RequestParam(name = "is_korean") Boolean is_korean,
			@RequestParam(name = "bio", required = false) String bio,
			@RequestParam(name = "mbti", required = false) MbtiCategory mbti,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages") Set<String> languages,
			@RequestParam(name = "profile_img", required = false) MultipartFile profile_img,
			@RequestParam(name = "verification_file") MultipartFile verification_file,
			@PathVariable(name = "id") Long id) {

		Member member =
				memberService.registerDetail(
						username, is_korean, bio, mbti, hobbies, languages, id, profile_img, verification_file);
		return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponseDto(member));
	}

	@GetMapping("/profile")
	@Operation(summary = "마이페이지 API", description = "로그인 한 유저의 개인 정보를 확인할 수 있는 마이페이지입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "마이페이지 정보 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = MemberResponseDto.class))
			})
	public ResponseEntity<MemberResponseDto> profile(Authentication auth) {
		Member currentMember = memberService.getMember(auth.getName());
		MemberResponseDto memberResponseDto = new MemberResponseDto(currentMember);
		return ResponseEntity.ok(memberResponseDto);
	}

	@PutMapping("/change-password")
	@Operation(
			summary = "비밀번호 변경 API",
			description = "이메일을 발송해 유저는 변경된 비밀번호를 받아 유효한 로그인을 진행할 수 있게 됩니다.")
	@ApiResponse(
			responseCode = "200",
			description = "비밀번호 변경 발송 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = VerifyEmailDto.class))
			})
	public ResponseEntity<HashMap> mailCheck(@RequestBody VerifyEmailDto emailDto) {
		boolean success = memberService.changePassword(emailDto);

		HashMap<String, Object> responseMap = new HashMap<>();

		if (success) {
			responseMap.put("status", 200);
			responseMap.put("message", "메일 발송 성공");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
		} else {
			responseMap.put("status", 500);
			responseMap.put("message", "메일 발송 실패");
			return new ResponseEntity<HashMap>(responseMap, HttpStatus.CONFLICT);
		}
	}
}
