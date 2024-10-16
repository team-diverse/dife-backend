package com.dife.api.controller;

import com.dife.api.model.dto.ChatRequestDto;
import com.dife.api.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SocketController {

	private final ChatService chatService;

	@MessageMapping("/chatroom/chat")
	public void sendMessage(
			ChatRequestDto dto,
			SimpMessageHeaderAccessor headerAccessor,
			@AuthenticationPrincipal UserDetails userDetails)
			throws JsonProcessingException {
		chatService.sendMessage(dto, headerAccessor, userDetails);
	}
}
