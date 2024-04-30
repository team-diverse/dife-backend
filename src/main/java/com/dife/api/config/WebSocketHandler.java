package com.dife.api.config;

import com.dife.api.model.Chatroom;
import com.dife.api.model.dto.ChatDto;
import com.dife.api.service.ChatService;
import com.dife.api.service.ChatroomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

	private final ChatroomService chatroomService;
	private final ChatService chatService;
	private final ObjectMapper objectMapper;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		ChatDto chat = objectMapper.readValue(payload, ChatDto.class);
		Chatroom chatroom = chatroomService.findChatroomById(chat.getChatroom_id(), session);
		chatroomService.handleActions(session, chat, chatService, chatroom);
	}
}
