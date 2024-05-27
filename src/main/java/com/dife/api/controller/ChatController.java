package com.dife.api.controller;

import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.ChatResponseDto;
import com.dife.api.service.ChatroomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
@Slf4j
public class ChatController implements SwaggerChatController {

	private final ChatroomService chatroomService;

	@GetMapping(value = "/{chatroomId}")
	public ResponseEntity<List<ChatResponseDto>> getChats(
			@PathVariable(name = "chatroomId") Long chatroomId, Authentication authentication) {
		List<ChatResponseDto> chats = chatroomService.getChats(chatroomId, authentication.getName());
		return ResponseEntity.status(OK).body(chats);
	}

	@GetMapping(value = "/{chatroomId}/")
	public ResponseEntity<ChatResponseDto> getChat(
			@PathVariable(name = "chatroomId") Long chatroomId,
			@RequestParam(name = "chatId") Long chatId,
			Authentication authentication) {
		ChatResponseDto responseDto =
				chatroomService.getChat(chatroomId, chatId, authentication.getName());
		return ResponseEntity.ok(responseDto);
	}
}
