package com.dife.api.controller;

import com.dife.api.model.dto.ChatDto;
import com.dife.api.model.dto.ChatEnterDto;
import com.dife.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SocketController {

	private final ChatService chatService;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccesor.getSessionId();

		log.info("새로운 세션 입장 by EventListener : {}", sessionId);
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccesor.getSessionId();

		log.info("세션 연결 끊김 by EventListener : {}", sessionId);
	}

	@MessageMapping("/chatroom/enter/{id}")
	@SendTo("/topic/chatroom")
	public void sendEnter(
			@DestinationVariable("id") Long id,
			ChatEnterDto dto,
			@Header("simpSessionId") String sessionId) {

		chatService.sendEnter(id, dto, sessionId);
	}

		chatService.sendMessage(id, dto);
	}
}
