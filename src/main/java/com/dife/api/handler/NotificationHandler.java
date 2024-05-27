package com.dife.api.handler;

import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@Configuration
@RequiredArgsConstructor
public class NotificationHandler {

	private final SimpMessageSendingOperations messagingTemplate;

	public void isAlone(Chatroom chatroom, String sessionId) {
		ChatroomSetting setting = chatroom.getChatroomSetting();
		if (setting.getCount() < 2) {
			notificate(chatroom.getId(), sessionId);
		}
	}

	public void notificate(Long chatroomId, String sessionId) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
		accessor.setSessionId(sessionId);
		accessor.setDestination("/sub/chatroom/" + chatroomId);
		messagingTemplate.convertAndSend(
				"/sub/chatroom/" + chatroomId, "해당 채팅방은 한 명만 남은 채팅방입니다!", accessor.getMessageHeaders());
	}
}
