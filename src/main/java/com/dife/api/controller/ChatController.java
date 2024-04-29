package com.dife.api.controller;

import com.dife.api.model.Chatroom;
import com.dife.api.model.dto.GroupChatroomRequestDto;
import com.dife.api.model.dto.GroupChatroomResponseDto;
import com.dife.api.service.ChatroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
@Slf4j
@Tag(name = "Chat API", description = "Chat API")
public class ChatController {

	private final ChatroomService chatroomService;

	@Operation(summary = "그룹 채팅방 생성", description = "사용자가 그룹 채팅방 생성")
	@PostMapping
	public ResponseEntity<GroupChatroomResponseDto> createGroupChatroom(
			@RequestBody(
							description = "채팅방 이름, 한줄소개, 최소 인원수, 최대 인원수를 포함하는 그룹 채팅방 생성 데이터",
							required = true,
							content = @Content(schema = @Schema(implementation = GroupChatroomRequestDto.class)))
					GroupChatroomRequestDto dto) {

		Chatroom chatroom = chatroomService.createGroupChatroom(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(new GroupChatroomResponseDto(chatroom));
	}
}
