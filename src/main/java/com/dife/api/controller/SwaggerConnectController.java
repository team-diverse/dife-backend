package com.dife.api.controller;

import com.dife.api.model.ConnectStatus;
import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Connect API", description = "커넥트 서비스 API")
public interface SwaggerConnectController {

	@Operation(
			summary = "커넥트 목록 조회 API",
			description = "회원 AUTH를 이용해 회원이 맺은 커넥트 목록을 커넥트의 status에 따라 조회합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 커넥트 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ConnectResponseDto.class))
			})
	ResponseEntity<List<ConnectResponseDto>> getConnects(
			@RequestParam(name = "status", required = false) ConnectStatus status, Authentication auth);

	@Operation(summary = "특정 회원과의 커넥트 조회 API", description = "특정 회원의 ID를 이용해 맺은 커넥트를 조회합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 커넥트 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ConnectResponseDto.class))
			})
	ResponseEntity<ConnectResponseDto> getConnect(
			@RequestParam(name = "member_id") Long memberId, Authentication auth);

	@Operation(summary = "커넥트 요청 API", description = "커넥트 맺고 싶은 회원의 ID를 이용해 요청을 생성합니다.")
	@ApiResponse(
			responseCode = "201",
			description = "커넥트 요청 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ConnectResponseDto.class))
			})
	ResponseEntity<ConnectResponseDto> createConnect(
			@Valid @RequestBody ConnectRequestDto requestDto, Authentication auth);

	@Operation(summary = "커넥트 수락 API", description = "요청온 특정 회원ID에 대한 커넥트 요청 수락 여부를 결정합니다. ")
	@ApiResponse(responseCode = "200", description = "커넥트 수락 성공 예시")
	ResponseEntity<Void> acceptConnect(
			@Valid @RequestBody ConnectPatchRequestDto requestDto, Authentication auth);

	@Operation(summary = "커넥트 삭제 API", description = "삭제하고픈 커넥트 ID를 입력해 삭제를 진행합니다.")
	@ApiResponse(responseCode = "200", description = "커넥트 삭제 성공 예시")
	ResponseEntity<Void> deleteConnect(@PathVariable(name = "id") Long id, Authentication auth);
}
