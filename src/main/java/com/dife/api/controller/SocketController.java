package com.dife.api.controller;

import com.dife.api.model.dto.ChatDto;
import com.dife.api.model.dto.ChatEnterDto;
import com.dife.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SocketController {

	private final ChatService chatService;

	@MessageMapping("/chatroom/enter/{id}")
	@SendTo("/topic/chatroom")
	public void sendEnter(
			@DestinationVariable("id") Long id,
			ChatEnterDto dto,
			@Header("simpSessionId") String sessionId)
			throws InterruptedException {

		chatService.enter(id, sessionId, dto);
	}

	@MessageMapping("/chatroom/chat/{id}")
	@SendTo("/topic/chatroom")
	public void sendMessage(
			@DestinationVariable("id") Long id, ChatDto dto, @Header("simpSessionId") String sessionId) {

		chatService.sendMessage(id, dto, sessionId);
	}
}
