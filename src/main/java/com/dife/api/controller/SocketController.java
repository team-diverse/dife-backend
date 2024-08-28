package com.dife.api.controller;

import com.dife.api.model.dto.ChatRequestDto;
import com.dife.api.service.ChatService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SocketController {

	private final ChatService chatService;

	@MessageMapping("/chatroom/chat")
	public void sendMessage(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws IOException {
		chatService.sendMessage(dto, headerAccessor);
	}
}
