package com.dife.api.controller;

import static org.springframework.http.HttpStatus.*;

import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.MbtiCategory;
import com.dife.api.model.dto.*;
import com.dife.api.service.MemberService;
import jakarta.validation.Valid;
import java.io.IOException;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController implements SwaggerMemberController {

	private final MemberService memberService;
	private final JWTUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDto> registerEmailAndPassword(
			@Valid @RequestBody RegisterEmailAndPasswordRequestDto dto) {
		RegisterResponseDto responseDto = memberService.registerEmailAndPassword(dto);

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@RequestMapping(value = "/check", method = RequestMethod.HEAD)
	public ResponseEntity<Void> checkUsername(
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "username", required = false) String username) {
		Boolean isDuplicate = memberService.isDuplicate(email, username);

		if (isDuplicate) {
			return ResponseEntity.status(CONFLICT).build();
		}
		return ResponseEntity.ok().build();
	}

	@PutMapping(consumes = "multipart/form-data")
	public ResponseEntity<MemberResponseDto> update(
			@RequestParam(name = "password", required = false) String password,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "country", required = false) String country,
			@RequestParam(name = "settingLanguage", required = false) String settingLanguage,
			@RequestParam(name = "bio", required = false) String bio,
			@RequestParam(name = "mbti", required = false) MbtiCategory mbti,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "verificationFile", required = false) MultipartFile verificationFile,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			Authentication auth) {

		MemberResponseDto responseDto =
				memberService.update(
						password,
						username,
						country,
						settingLanguage,
						bio,
						mbti,
						hobbies,
						languages,
						isPublic,
						profileImg,
						verificationFile,
						auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MemberResponseDto> getById(
			@PathVariable(name = "id") Long id, Authentication auth) {
		MemberResponseDto responseDto = memberService.getMemberById(id, auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/profile")
	public ResponseEntity<MemberResponseDto> profile(Authentication auth) {
		MemberResponseDto responseDto = memberService.getMember(auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginSuccessDto> login(@Valid @RequestBody LoginDto dto) {
		return memberService.login(dto);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<Void> checkToken(@Valid @RequestBody RefreshTokenRequestDto requestDto) {

		boolean isTokenExpired = jwtUtil.isExpired(requestDto.getToken());
		if (isTokenExpired) {
			return ResponseEntity.status(UNAUTHORIZED).build();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/change-password")
	public ResponseEntity<Void> sendChangeVerify(@RequestParam(name = "email") String email) {
		memberService.changePassword(email);

		return new ResponseEntity<>(OK);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<Void> changePassword(
			@RequestParam(name = "verifyCode", required = false) String verifyCode,
			@RequestParam(name = "newPassword", required = false) String newPassword,
			@RequestParam(name = "email", required = false) String email) {
		memberService.verifyChangePasswordCode(verifyCode, newPassword, email);

		return new ResponseEntity<>(OK);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteMember(Authentication auth) {
		memberService.deleteMember(auth.getName());
		return new ResponseEntity<>(OK);
	}

	@GetMapping("/random")
	public ResponseEntity<List<MemberResponseDto>> getRandomMembers(
			@RequestParam(name = "count", defaultValue = "1") int count, Authentication auth)
			throws IOException {
		List<MemberResponseDto> responseDto = memberService.getRandomMembers(count, auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<MemberResponseDto>> getFilterMembers(
			@RequestParam(name = "mbtis", required = false) Set<MbtiCategory> mbtiCategories,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			Authentication auth) {
		List<MemberResponseDto> responseDto =
				memberService.getFilterMembers(mbtiCategories, hobbies, languages, auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/search")
	public ResponseEntity<List<MemberResponseDto>> getSearchMembers(
			@RequestParam(name = "keyword") String keyword, Authentication auth) {
		List<MemberResponseDto> responseDto = memberService.getSearchMembers(keyword, auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostResponseDto>> getMemberPosts(Authentication auth) {
		List<PostResponseDto> responseDto = memberService.getMemberPosts(auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/comments")
	public ResponseEntity<List<CommentResponseDto>> getMemberComments(Authentication auth) {
		List<CommentResponseDto> responseDto = memberService.getComments(auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping("/likes")
	public ResponseEntity<List<MemberResponseDto>> getLikeMembers(Authentication auth) {
		List<MemberResponseDto> responseDto = memberService.getLikeMembers(auth.getName());
		return ResponseEntity.ok(responseDto);
	}
}
