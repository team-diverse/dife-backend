package com.dife.api.controller;

import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Comment API", description = "댓글 서비스 API")
public interface SwaggerBlockController {

	@Operation(summary = "차단 생성 API", description = "사용자가 차단하고자 하는 회원의 ID를 body에 작성해 차단 생성하는 API입니다.")
	@ApiResponse(responseCode = "201")
	ResponseEntity<List<MemberResponseDto>> createBlock(
			BlockMemberRequestDto requestDto, Authentication auth);

	@Operation(summary = "차단 목록 조회 API", description = "사용자가 차단되어 있는 회원들의 목록을 조회할 수 있는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<MemberResponseDto>> createBlock(Authentication auth);

	@Operation(
			summary = "차단 취소 API",
			description = "사용자가 회원 차단을 취소하는 API입니다.파라미터로 memberId를 받아 차단 회원을 취소합니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<Void> deleteBlock(
			@RequestParam(name = "memberId") Long memberId, Authentication auth);
}
