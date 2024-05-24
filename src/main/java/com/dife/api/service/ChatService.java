package com.dife.api.service;

import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.redis.RedisPublisher;
import com.dife.api.repository.ChatRepository;
import com.dife.api.repository.ChatroomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatroomService chatroomService;
	private final ChatroomRepository chatroomRepository;
	private final RedisPublisher redisPublisher;
	private final ChatRepository chatRepository;

	public void sendMessage(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException, InterruptedException {
		switch (dto.getChatType()) {
			case ENTER:
				enter(dto, headerAccessor);
			case CHAT:
				chat(dto, headerAccessor);
				break;
			case EXIT:
				exit(dto, headerAccessor);
		}
	}

	public void disconnectSession(Long chatroom_id, String session_id) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		accessor.setSessionId(session_id);
		accessor.setDestination("/sub/chatroom/" + chatroom_id);
		messagingTemplate.convertAndSend(
				"/sub/chatroom/" + chatroom_id, "Disconnect", accessor.getMessageHeaders());
	}

	public Chatroom validChatroom(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor) {
		Long chatroom_id = dto.getChatroomId();
		String session_id = headerAccessor.getSessionId();

		Boolean is_valid = chatroomRepository.existsById(chatroom_id);
		if (!is_valid) {
			disconnectSession(chatroom_id, session_id);
		}
		return chatroomService.getChatroom(chatroom_id);
	}

	public void enter(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Chatroom chatroom = validChatroom(dto, headerAccessor);
		Long chatroom_id = chatroom.getId();
		String session_id = headerAccessor.getSessionId();
		Boolean validGroupChatroom =
				(chatroom.getChatroomType() == ChatroomType.GROUP
						&& !chatroom.getChatroom_setting().getIs_public()
						&& chatroomService.isWrongPassword(chatroom, dto.getPassword()));

		ChatroomSetting setting = chatroom.getChatroom_setting();

		if (chatroomService.isFull(chatroom)) {
			disconnectSession(chatroom_id, session_id);
			return;
		}

		if (validGroupChatroom) {
			disconnectSession(chatroom_id, session_id);
			return;
		}

		Map<String, String> activeSessions = chatroom.getActiveSessions();

		if (!activeSessions.containsKey(session_id)) {
			activeSessions.put(session_id, dto.getUsername());
			chatroom.setActiveSessions(activeSessions);
			Integer nCount = setting.getCount();
			setting.setCount(nCount + 1);
			chatroom.setChatroom_setting(setting);

			headerAccessor.getSessionAttributes().put("session_id", session_id);
			headerAccessor.getSessionAttributes().put("chatroom_id", chatroom_id);

			redisPublisher.publish(dto);
		}
		chatroomRepository.save(chatroom);
	}

	public void chat(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Chatroom chatroom = validChatroom(dto, headerAccessor);

		if (dto.getMessage().length() <= 300) {
			Chat chat = new Chat();
			chat.setMessage(dto.getMessage());
			chat.setChatroom(chatroom);

			chatRepository.save(chat);
			redisPublisher.publish(dto);
		}
	}

	public void exit(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException, InterruptedException {

		Chatroom chatroom = validChatroom(dto, headerAccessor);
		Long chatroom_id = dto.getChatroomId();
		String session_id = headerAccessor.getSessionId();

		Map<String, String> activeSessions = chatroom.getActiveSessions();
		activeSessions.remove(session_id);

		ChatroomSetting setting = chatroom.getChatroom_setting();
		Integer nCount = setting.getCount();
		nCount--;
		setting.setCount(nCount);

		chatroom.setChatroom_setting(setting);
		chatroomRepository.save(chatroom);
		disconnectSession(chatroom_id, session_id);
		redisPublisher.publish(dto);
		Thread.sleep(1000);

		if (nCount < 2) {

			StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
			accessor.setSessionId(session_id);
			accessor.setDestination("/sub/chatroom/" + chatroom_id);
			messagingTemplate.convertAndSend(
					"/sub/chatroom/" + chatroom_id, "해당 채팅방은 한 명만 남은 채팅방입니다!", accessor.getMessageHeaders());
		}
	}
}
