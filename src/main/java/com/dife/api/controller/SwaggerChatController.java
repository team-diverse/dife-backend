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
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Chat API", description = "채팅 관리 서비스 API")
public interface SwaggerChatController {

	@Operation(summary = "채팅방의 채팅목록 가져오기 API", description = "채팅방의 ID를 입력하여 채팅목록을 가져옵니다.")
	@ApiResponse(
			responseCode = "201",
			description = "채팅 목록 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatResponseDto.class))
			})
	ResponseEntity<List<ChatResponseDto>> getChats(
			@RequestParam(name = "chatroomId") Long chatroomId, Authentication authentication);

	@Operation(summary = "단일 채팅 가져오기 API", description = "채팅방의 ID와 채팅 ID를 입력하여 단일 채팅을 가져옵니다.")
	@ApiResponse(
			responseCode = "201",
			description = "단일 채팅 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatResponseDto.class))
			})
	ResponseEntity<ChatResponseDto> getChat(
			@RequestParam(name = "chatroomId") Long chatroomId,
			@RequestParam(name = "chatId") Long chatId,
			Authentication authentication);
}
