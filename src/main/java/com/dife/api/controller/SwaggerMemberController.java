package com.dife.api.controller;

import com.dife.api.model.MbtiCategory;
import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface SwaggerMemberController {

	@Operation(summary = "회원가입1 API", description = "이메일과 비밀번호를 사용하여 새 멤버를 생성합니다.")
	@ApiResponse(
			responseCode = "201",
			description = "회원가입1 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = RegisterResponseDto.class))
			})
	ResponseEntity<RegisterResponseDto> registerEmailAndPassword(
			RegisterEmailAndPasswordRequestDto dto);

	@Operation(summary = "중복 닉네임 확인", description = "회원가입 세부사항을 입력하기에 앞서 중복 닉네임 여부를 확인합니다.")
	@ApiResponse(responseCode = "200", description = "중복 닉네임 확인 성공 예시")
	ResponseEntity<Void> checkUsername(@RequestParam(name = "username") String username);

	@Operation(summary = "회원가입2 API", description = "회원가입 세부사항을 입력해 회원등록을 마무리합니다.")
	@ApiResponse(
			responseCode = "201",
			description = "회원가입2 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = MemberResponseDto.class))
			})
	ResponseEntity<MemberResponseDto> registerDetail(
			@RequestParam(name = "username") String username,
			@RequestParam(name = "isKorean") Boolean isKorean,
			@RequestParam(name = "bio", required = false) String bio,
			@RequestParam(name = "mbti", required = false) MbtiCategory mbti,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages") Set<String> languages,
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "verificationFile", required = false) MultipartFile verificationFile,
			@RequestParam(name = "isPublic") Boolean isPublic,
			@PathVariable(name = "id") Long id);

	@Operation(summary = "마이페이지 API", description = "로그인 한 유저의 개인 정보를 확인할 수 있는 마이페이지 입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "마이페이지 정보 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = MemberResponseDto.class))
			})
	ResponseEntity<MemberResponseDto> profile(Authentication auth);

	@Operation(
			summary = "로그인 API",
			description = "로그인 성공시 주어지는 회원 ID, AccessToken, RefreshToken 정보를 확인할 수 있습니다.")
	@ApiResponse(
			responseCode = "200",
			description = "로그인 성공 정보 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = LoginSuccessDto.class))
			})
	ResponseEntity<LoginSuccessDto> login(LoginDto dto);

	@Operation(summary = "토큰 확인 API", description = "토큰의 입력을 받아 만료 여부를 알 수 있게 합니다.")
	@ApiResponse(responseCode = "200", description = "비밀번호 변경 발송 예시")
	ResponseEntity<Void> checkToken(RefreshTokenRequestDto requestDto);

	@Operation(
			summary = "비밀번호 변경 API",
			description = "이메일을 발송해 유저는 변경된 비밀번호를 받아 유효한 로그인을 진행할 수 있게 됩니다.")
	@ApiResponse(responseCode = "200", description = "비밀번호 변경 발송 예시")
	ResponseEntity<Void> changePassword(@RequestParam(name = "email") String email);

	@Operation(
			summary = "홈화면 랜덤 10개 회원 조회 API",
			description = "홈화면에 보일 커넥트 준비상태 10개 회원 정보를 확인할 수 있습니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<MemberResponseDto>> getRandomMembers(
			@RequestParam(name = "count", defaultValue = "1") int count, Authentication auth);
}
