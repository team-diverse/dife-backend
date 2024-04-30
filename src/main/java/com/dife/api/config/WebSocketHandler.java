package com.dife.api.config;

import com.dife.api.model.Chatroom;
import com.dife.api.model.dto.ChatDto;
import com.dife.api.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

	private final ChatroomService chatroomService;
	private final ModelMapper modelMapper;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		ChatDto chat = modelMapper.map(payload, ChatDto.class);
		Chatroom chatroom = chatroomService.findChatroomById(chat.getChatroom_id(), session);
	}
}
