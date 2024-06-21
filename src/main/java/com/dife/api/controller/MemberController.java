package com.dife.api.controller;

import static org.springframework.http.HttpStatus.*;

import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.MbtiCategory;
import com.dife.api.model.dto.*;
import com.dife.api.service.MemberService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class MemberController implements SwaggerMemberController {

	private final MemberService memberService;
	private final JWTUtil jwtUtil;

	@PostMapping(value = "/register", consumes = "application/json")
	public ResponseEntity<RegisterResponseDto> registerEmailAndPassword(
			@Valid @RequestBody RegisterEmailAndPasswordRequestDto dto) {
		RegisterResponseDto responseDto = memberService.registerEmailAndPassword(dto);

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@GetMapping
	public ResponseEntity<Void> checkUsername(@RequestParam(name = "username") String username) {
		Boolean isValid = memberService.checkUsername(username);

		if (isValid) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(CONFLICT).build();
	}

	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<MemberResponseDto> update(
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "isKorean", required = false) Boolean isKorean,
			@RequestParam(name = "bio", required = false) String bio,
			@RequestParam(name = "mbti", required = false) MbtiCategory mbti,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "verificationFile") MultipartFile verificationFile,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			@PathVariable(name = "id") Long id) {

		MemberResponseDto responseDto =
				memberService.registerDetail(
						username,
						isKorean,
						bio,
						mbti,
						hobbies,
						languages,
						isPublic,
						id,
						profileImg,
						verificationFile);
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping("/profile")
	public ResponseEntity<MemberResponseDto> profile(Authentication auth) {
		MemberResponseDto responseDto = memberService.getMember(auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@PostMapping(value = "/login", consumes = "application/json")
	public ResponseEntity<LoginSuccessDto> login(@Valid @RequestBody LoginDto dto) {
		return memberService.login(dto);
	}

	@PostMapping(value = "/refresh-token", consumes = "application/json")
	public ResponseEntity<Void> checkToken(@Valid @RequestBody RefreshTokenRequestDto requestDto) {

		boolean isTokenExpired = jwtUtil.isExpired(requestDto.getToken());
		if (isTokenExpired) {
			return ResponseEntity.status(UNAUTHORIZED).build();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/change-password")
	public ResponseEntity<Void> changePassword(@RequestParam(name = "email") String email) {
		memberService.changePassword(email);

		return new ResponseEntity<>(OK);
	}

	@GetMapping("/random")
	public ResponseEntity<List<MemberResponseDto>> getRandomMembers(
			@RequestParam(name = "count", defaultValue = "1") int count, Authentication auth) {
		List<MemberResponseDto> responseDto = memberService.getRandomMembers(count, auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<MemberResponseDto>> getFilterMembers(
			@RequestParam(name = "mbtis", required = false) Set<MbtiCategory> mbtiCategories,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages) {
		List<MemberResponseDto> responseDto =
				memberService.getFilterMembers(mbtiCategories, hobbies, languages);
		return ResponseEntity.ok(responseDto);
	}
}
