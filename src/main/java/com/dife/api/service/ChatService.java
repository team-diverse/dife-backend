package com.dife.api.service;

import com.dife.api.model.Chat;
import com.dife.api.model.ChatType;
import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.dto.ChatDto;
import com.dife.api.model.dto.ChatEnterDto;
import com.dife.api.redis.RedisPublisher;
import com.dife.api.redis.RedisSubscriber;
import com.dife.api.repository.ChatRepository;
import com.dife.api.repository.ChatroomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
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
	private final ChatRepository chatRepository;
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final RedisSubscriber redisSubscriber;
	private final RedisPublisher redisPublisher;
	private final ChannelTopic topic;

	public void sendMessage(Long room_id, ChatDto dto, String session_id) {

		switch (dto.getChatType()) {
			case CHAT:
				chat(room_id, session_id, dto);
				break;
			case EXIT:
				exit(room_id, session_id, dto);
		}
	}

	public void disconnectSession(Long room_id, String session_id) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		accessor.setSessionId(session_id);
		accessor.setDestination("/sub/chatroom/" + room_id);
		messagingTemplate.convertAndSend(
				"/sub/chatroom/" + room_id, "Disconnect", accessor.getMessageHeaders());
	}

	public void sendEnter(ChatEnterDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {
		Long room_id = dto.getChatroom_id();

		String session_id = headerAccessor.getSessionId();


		Boolean is_valid = chatroomService.findChatroomById(room_id);
		if (!is_valid) {
			disconnectSession(room_id, session_id);
			return;
		}
		if (dto.getChatType() != ChatType.ENTER) {
			disconnectSession(room_id, session_id);
			return;
		}

		Chatroom chatroom = chatroomService.getChatroom(room_id);
		if (chatroomService.isFull(chatroom)) {
			disconnectSession(room_id, session_id);
			return;
		}
		if (chatroomService.isWrongPassword(chatroom, dto.getPassword())) {
			disconnectSession(room_id, session_id);
			return;
		}

		Map<String, String> activeSessions = chatroom.getActiveSessions();

		if (!activeSessions.containsKey(session_id)) {
			activeSessions.put(session_id, dto.getSender());
			ChatroomSetting setting = chatroom.getChatroom_setting();
			Integer nCount = setting.getCount();
			nCount++;
			setting.setCount(nCount);

			headerAccessor.getSessionAttributes().put("session_id", session_id);
			headerAccessor.getSessionAttributes().put("chatroom_id", room_id);

			redisPublisher.publish(dto);
		}
		chatroomRepository.save(chatroom);
	}

	public void chat(Long room_id, String session_id, ChatDto dto) {
		Boolean is_valid = chatroomService.findChatroomById(room_id);

		if (!is_valid) {
			disconnectSession(room_id, session_id);
			return;
		}
		Chatroom chatroom = chatroomService.getChatroom(room_id);

		Map<String, String> activeSessions = chatroom.getActiveSessions();

		if (!activeSessions.containsKey(session_id)) {
			disconnectSession(room_id, session_id);
			return;
		}

		if (dto.getMessage().length() > 300) {
			messagingTemplate.convertAndSend("/topic/chatroom/" + room_id, "메시지는 300자 이내로 입력하셔야 합니다.");
		} else {
			Chat chat = new Chat();
			chat.setMessage(dto.getMessage());
			chat.setChatroom(chatroom);
			chat.setSender(dto.getSender());

			chatRepository.save(chat);
			messagingTemplate.convertAndSend("/topic/chatroom/" + room_id, dto.getMessage());
		}
	}

	public void exit(Long room_id, String session_id, ChatDto dto) {
		Boolean is_valid = chatroomService.findChatroomById(room_id);
		if (!is_valid) {
			disconnectSession(room_id, session_id);
			return;
		}

		Chatroom chatroom = chatroomService.getChatroom(room_id);

		Map<String, String> activeSessions = chatroom.getActiveSessions();

		if (!activeSessions.containsKey(session_id)) {
			disconnectSession(room_id, session_id);
			return;
		}

		activeSessions.remove(session_id);
		ChatroomSetting setting = chatroom.getChatroom_setting();
		Integer nCount = setting.getCount();
		nCount--;
		setting.setCount(nCount);
		messagingTemplate.convertAndSend(
				"/topic/chatroom/" + chatroom.getId(), dto.getSender() + "님이 퇴장하셨습니다!");

		if (nCount < 2) {
			chatroomRepository.delete(chatroom);
			disconnectSession(room_id, session_id);
			return;
		}

		chatroom.setActiveSessions(activeSessions);
		chatroomRepository.save(chatroom);
		disconnectSession(room_id, session_id);
	}
}
