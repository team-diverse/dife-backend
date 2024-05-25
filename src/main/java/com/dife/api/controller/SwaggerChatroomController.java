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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chatroom API", description = "채팅방 관리 서비스 API")
public interface SwaggerChatroomController {

	@Operation(summary = "채팅방 생성1 API", description = "사용자가 DTO를 작성해 POST요청으로 그룹 채팅방1 생성")
	@ApiResponse(
			responseCode = "201",
			description = "그룹 채팅방1 생성 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<ChatroomResponseDto> createChatroom(
			ChatroomPostRequestDto requestDto, Authentication authentication);

	@Operation(summary = "채팅방 생성2 API", description = "사용자가 DTO를 작성해 PUT요청으로 그룹 채팅방2 생성")
	@ApiResponse(
			responseCode = "201",
			description = "그룹 채팅방2 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<ChatroomResponseDto> registerDetail(
			GroupChatroomPutRequestDto requestDto,
			@PathVariable(name = "chatroomId") Long chatroomId,
			Authentication auth);

	@Operation(
			summary = "채팅방 전체 조회 API",
			description = "조회하고자 하는 채팅방 타입(그룹/싱글)을 입력해 속한 채팅방들을 조회하는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "채팅방 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<List<ChatroomResponseDto>> getGroupChatrooms(
			@RequestBody ChatroomTypeRequestDto requestDto, Authentication authentication);

	@Operation(summary = "채팅방 조회 API", description = "그룹, 싱글 모든 채팅방을 Id로 조회해주는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "채팅방 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<ChatroomResponseDto> getGroupChatroom(@PathVariable(name = "id") Long id);
}
