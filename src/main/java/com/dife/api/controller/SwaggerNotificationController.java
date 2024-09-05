package com.dife.api.controller;

import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Notification API", description = "알림 서비스 API")
public interface SwaggerNotificationController {

	@Operation(summary = "알림 토큰 조회 API", description = "인가를 이용해 사용자의 모든 종류의 알림 토큰을 조회하는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 알림 토큰 조회 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = NotificationTokenResponseDto.class))
			})
	ResponseEntity<List<NotificationTokenResponseDto>> getNotificationTokens(Authentication auth);

	@Operation(summary = "알림 리스트 조회 API", description = "인가를 이용해 사용자의 알림을 조회하는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 알림 조회 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = NotificationResponseDto.class))
			})
	ResponseEntity<List<NotificationResponseDto>> getNotifications(
			@PathVariable("deviceId") String deviceId, Authentication auth);

	@Operation(summary = "알림 토큰 생성 API", description = "사용자의 접속 기기에 따른 알림 토큰을 생성하는 API입니다.")
	@ApiResponse(
			responseCode = "201",
			description = "알림 토큰 생성 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = NotificationTokenResponseDto.class))
			})
	ResponseEntity<NotificationTokenResponseDto> createNotificationToken(
			NotificationTokenRequestDto requestDto, Authentication auth);

	@Operation(summary = "알림 삭제 API", description = "알림의 ID를 입력해 알림을 삭제할 수 있는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<Void> deleteNotification(@PathVariable("id") Long id, Authentication auth);
}
