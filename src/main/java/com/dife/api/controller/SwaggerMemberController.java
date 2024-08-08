package com.dife.api.controller;

import com.dife.api.model.MbtiCategory;
import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member API", description = "회원 서비스 API")
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
			@Valid @RequestBody RegisterEmailAndPasswordRequestDto dto);

	@Operation(summary = "중복 이메일/닉네임 확인", description = "회원가입 세부사항을 입력하기에 앞서 중복 이메일/닉네임 여부를 확인합니다.")
	@ApiResponse(responseCode = "200", description = "중복 이메일/닉네임 확인 성공 예시")
	ResponseEntity<Void> checkUsername(CheckDuplicateRequestDto requestDto);

	@Operation(summary = "회원정보 업데이트 API", description = "회원가입 세부사항을 입력해 회원등록을 업데이트 합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "회원정보 업데이트 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = MemberResponseDto.class))
			})
	ResponseEntity<MemberResponseDto> update(
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "country", required = false) String country,
			@RequestParam(name = "bio", required = false) String bio,
			@RequestParam(name = "mbti", required = false) MbtiCategory mbti,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "verificationFile", required = false) MultipartFile verificationFile,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			Authentication auth);

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
	ResponseEntity<LoginSuccessDto> login(@Valid @RequestBody LoginDto dto);

	@Operation(summary = "토큰 확인 API", description = "토큰의 입력을 받아 만료 여부를 알 수 있게 합니다.")
	@ApiResponse(responseCode = "200", description = "비밀번호 변경 발송 예시")
	ResponseEntity<Void> checkToken(@Valid @RequestBody RefreshTokenRequestDto requestDto);

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

	@Operation(
			summary = "필터 선택지 회원 조회 API",
			description =
					"세부적인 회원 조회 필터링 선택지를 사용자에게 제시해 해당하는 회원을 조회할 수 있게 됩니다. MBTI, 취미, 언어의 복수 선택, 단일 종류 선택 가능한 name Set을 입력받게 됩니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<MemberResponseDto>> getFilterMembers(
			@RequestParam(name = "mbtis", required = false) Set<MbtiCategory> mbtiCategories,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages);

	@Operation(
			summary = "회원 필터 검색 조회 API",
			description = "회원의 이름, 닉네임, 전공, 학번, 한줄 소개에 해당 검색어가 포함되는 회원들을 조회하는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<MemberResponseDto>> getSearchMembers(
			@RequestParam(name = "keyword") String keyword);

	@Operation(summary = "회원이 작성한 게시글 조회 API", description = "회원의 인가를 이용해 작성한 게시글을 조회하는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<PostResponseDto>> getMemberPosts(Authentication auth);

	@Operation(summary = "회원이 작성한 댓글 조회 API", description = "회원의 인가를 이용해 작성한 댓글을 조회하는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<CommentResponseDto>> getMemberComments(Authentication auth);
}
