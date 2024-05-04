package com.dife.api.controller;

import com.dife.api.model.*;
import com.dife.api.model.dto.GroupChatroomRequestDto;
import com.dife.api.model.dto.GroupChatroomResponseDto;
import com.dife.api.service.ChatroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
@Slf4j
public class ChatController {

	private final ChatroomService chatroomService;

	@Operation(summary = "그룹 채팅방 생성", description = "사용자가 그룹 채팅방 생성")
	@PostMapping(value = "/", consumes = "multipart/form-data")
	@ApiResponse(
			responseCode = "201",
			description = "그룹 생성방1 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = GroupChatroomRequestDto.class))
			})
	public ResponseEntity<GroupChatroomRequestDto> createGroupChatroom(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam("description") String description,
			@RequestParam("profile_img") MultipartFile profile_img) {

		Chatroom chatroom = chatroomService.createGroupChatroom(name, description, profile_img);

		return ResponseEntity.status(HttpStatus.CREATED).body(new GroupChatroomRequestDto(chatroom));
	}

	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	@ApiResponse(
			responseCode = "201",
			description = "그룹 채팅방2 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = GroupChatroomResponseDto.class))
			})
	public ResponseEntity<GroupChatroomResponseDto> registerDetail(
			@RequestParam(value = "tags", required = false) Set<String> tags,
			@RequestParam(value = "min_count", required = false) Integer min_count,
			@RequestParam(value = "max_count", required = false) Integer max_count,
			@RequestParam(value = "languages", required = false) Set<String> languages,
			@RequestParam(value = "purposes", required = false) Set<String> purposes,
			@RequestParam(value = "is_public", required = false) Boolean is_public,
			@RequestParam(value = "password", required = false) String password,
			@PathVariable Long id) {

		Chatroom chatroom =
				chatroomService.registerDetail(
						tags, min_count, max_count, languages, purposes, is_public, password, id);
		return ResponseEntity.status(HttpStatus.CREATED).body(new GroupChatroomResponseDto(chatroom));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GroupChatroomResponseDto> getGroupChatroom(@PathVariable Long id) {
		Chatroom chatroom = chatroomService.getChatroom(id);
		return ResponseEntity.status(HttpStatus.OK).body(new GroupChatroomResponseDto(chatroom));
	}
}
