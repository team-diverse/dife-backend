package com.dife.api.controller;

import com.dife.api.model.ChatroomType;
import com.dife.api.model.dto.*;
import com.dife.api.service.ChatroomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
@Slf4j
public class ChatroomController implements SwaggerChatroomController {

	private final ChatroomService chatroomService;

	@GetMapping
	public ResponseEntity<List<ChatroomResponseDto>> getGroupChatrooms(
			ChatroomType chatroomType, Authentication authentication) {
		List<ChatroomResponseDto> responseDto =
				chatroomService.getChatrooms(chatroomType, authentication.getName());
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChatroomResponseDto> getGroupChatroom(
			@PathVariable(name = "id") Long chatroomId) {
		ChatroomResponseDto responseDto = chatroomService.getChatroom(chatroomId);
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<ChatroomResponseDto> createChatroom(
			@RequestBody ChatroomPostRequestDto requestDto, Authentication authentication) {

		ChatroomResponseDto responseDto =
				chatroomService.createChatroom(requestDto, authentication.getName());

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@PutMapping(value = "/{id}", consumes = "application/json")
	public ResponseEntity<ChatroomResponseDto> registerDetail(
			@RequestBody GroupChatroomPutRequestDto requestDto,
			@PathVariable(name = "id") Long chatroomId,
			Authentication auth) {

		ChatroomResponseDto responseDto =
				chatroomService.registerDetail(requestDto, chatroomId, auth.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
}
