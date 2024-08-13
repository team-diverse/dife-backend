package com.dife.api.controller;

import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.ChatResponseDto;
import com.dife.api.service.ChatService;
import com.dife.api.service.ChatroomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
@Slf4j
public class ChatController implements SwaggerChatController {

	private final ChatroomService chatroomService;
	private final ChatService chatService;

	@GetMapping
	public ResponseEntity<List<ChatResponseDto>> getChats(
			@RequestParam(name = "chatroomId") Long chatroomId, Authentication authentication) {
		List<ChatResponseDto> chats = chatroomService.getChats(chatroomId, authentication.getName());
		return ResponseEntity.status(OK).body(chats);
	}

	@GetMapping("/")
	public ResponseEntity<ChatResponseDto> getChat(
			@RequestParam(name = "chatroomId") Long chatroomId,
			@RequestParam(name = "chatId") Long chatId,
			Authentication authentication) {
		ChatResponseDto responseDto =
				chatroomService.getChat(chatroomId, chatId, authentication.getName());
		return ResponseEntity.ok(responseDto);
	}

	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<ChatResponseDto> addChatFiles(
			@RequestParam(name = "chatFiles", required = false) List<MultipartFile> chatFiles,
			@RequestParam(name = "chatroomId") Long chatroomId,
			Authentication auth)
			throws JsonProcessingException {
		ChatResponseDto responseDto = chatService.addChatFiles(chatFiles, chatroomId, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}
}
