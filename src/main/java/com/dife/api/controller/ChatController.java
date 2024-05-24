package com.dife.api.controller;

import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.ChatGetRequestDto;
import com.dife.api.model.dto.ChatResponseDto;
import com.dife.api.model.dto.ChatsGetByChatroomRequestDto;
import com.dife.api.service.ChatroomService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
@Slf4j
public class ChatController {

	private final ChatroomService chatroomService;

	@GetMapping
	public ResponseEntity<List<ChatResponseDto>> getChats(
			@Valid ChatsGetByChatroomRequestDto requestDto, Authentication authentication) {
		List<ChatResponseDto> chats = chatroomService.getChats(requestDto, authentication.getName());
		return ResponseEntity.status(OK).body(chats);
	}

	@GetMapping("/")
	public ResponseEntity<ChatResponseDto> getChat(
			@Valid ChatGetRequestDto requestDto, Authentication authentication) {
		ChatResponseDto responseDto = chatroomService.getChat(requestDto, authentication.getName());
		return ResponseEntity.ok(responseDto);
	}
}
