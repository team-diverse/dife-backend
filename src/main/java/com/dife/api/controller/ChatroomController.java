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
import org.springframework.web.multipart.MultipartFile;

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

	@PostMapping
	public ResponseEntity<ChatroomResponseDto> createChatroom(
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "chatroomType") ChatroomType chatroomType,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "toMemberId", required = false) Long toMemberId,
			Authentication authentication) {

		ChatroomResponseDto responseDto =
				chatroomService.createChatroom(
						profileImg, chatroomType, name, description, toMemberId, authentication.getName());

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
