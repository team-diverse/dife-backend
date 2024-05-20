package com.dife.api.controller;

import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.service.ChatroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Set;
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
public class ChatroomController {

	private final ChatroomService chatroomService;

	@Operation(summary = "채팅방 생성", description = "사용자가 그룹 채팅방 생성")
	@PostMapping(value = "/", consumes = "multipart/form-data")
	@ApiResponse(
			responseCode = "201",
			description = "그룹 생성방1 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	public ResponseEntity<ChatroomResponseDto> createGroupChatroom(
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "chatroom_type") ChatroomType type) {

		Chatroom chatroom = chatroomService.createChatroom(name, description, type);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ChatroomResponseDto(chatroom));
	}

	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	@ApiResponse(
			responseCode = "201",
			description = "그룹 채팅방2 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	public ResponseEntity<ChatroomResponseDto> registerDetail(
			@RequestParam(name = "tags") Set<String> tags,
			@RequestParam(name = "max_count") Integer max_count,
			@RequestParam(name = "languages") Set<String> languages,
			@RequestParam(name = "purposes") Set<String> purposes,
			@RequestParam(name = "is_public") Boolean is_public,
			@RequestParam(name = "password", required = false) String password,
			@PathVariable(name = "id") Long id) {

		Chatroom chatroom =
				chatroomService.registerDetail(
						tags, max_count, languages, purposes, is_public, password, id);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ChatroomResponseDto(chatroom));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChatroomResponseDto> getGroupChatroom(@PathVariable(name = "id") Long id) {
		Chatroom chatroom = chatroomService.getChatroom(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ChatroomResponseDto(chatroom));
	}

	@Operation(summary = "싱글 채팅방 생성", description = "사용자가 싱글 채팅방 생성")
	@PostMapping("/single/")
	@ApiResponse(
			responseCode = "201",
			description = "싱글 채팅방 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	public ResponseEntity<ChatroomResponseDto> createSingleChatroom() {

		Chatroom chatroom = chatroomService.createSingleChatroom();

		return ResponseEntity.status(HttpStatus.CREATED).body(new ChatroomResponseDto(chatroom));
	}

	@GetMapping("/chatlist")
	public ResponseEntity<ChatResponseDto> getChat(
			@RequestParam(name = "room_id") Long room_id, @RequestParam(name = "chat_id") Long chat_id) {
		Chat chat = chatroomService.getChat(room_id, chat_id);
		return ResponseEntity.ok(new ChatResponseDto(chat));
	}

	@GetMapping("/bookmarklist/")
	public ResponseEntity<List<BookmarklistDto>> getBookmarks(
			@RequestParam(name = "room_id") Long room_id) {
		List<BookmarklistDto> scraps = chatroomService.getBookmarks(room_id);
		return ResponseEntity.ok(scraps);
	}

	@GetMapping("/bookmarklist")
	public ResponseEntity<ChatResponseDto> getBookmark(
			@RequestParam(name = "room_id") Long room_id,
			@RequestParam(name = "bookmark_id") Long bookmark_id) {
		Bookmark bookmark = chatroomService.getBookmark(room_id, bookmark_id);
		return ResponseEntity.ok(new ChatResponseDto(bookmark));
	}
}
