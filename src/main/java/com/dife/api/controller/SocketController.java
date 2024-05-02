package com.dife.api.controller;

import com.dife.api.model.dto.ChatDto;
import com.dife.api.service.ChatService;
import com.dife.api.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
public class SocketController {

	private final ChatroomService chatroomService;
	private final ChatService chatService;
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketController.class);

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		LOGGER.info("Received a new web socket connection");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccesor.getSessionId();

		LOGGER.info("sessionId Disconnected : " + sessionId);
	}

	@MessageMapping("/chatroom/{id}")
	@SendTo("/topic/chatroom")
	public void sendMessage(@DestinationVariable("id") Long id, ChatDto dto) {

		chatService.sendMessage(id, dto);
	}
}
