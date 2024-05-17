package com.dife.api.service;

import com.dife.api.model.Chat;
import com.dife.api.model.ChatType;
import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.dto.ChatDto;
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
	private final ChatRepository chatRepository;
	private final RedisPublisher redisPublisher;

	public void sendMessage(ChatDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		switch (dto.getChatType()) {
			case CHAT:
				chat(dto, headerAccessor);
				break;
			case EXIT:
				exit(dto, headerAccessor);
		}
	}

	public void disconnectSession(Long room_id, String session_id) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		accessor.setSessionId(session_id);
		accessor.setDestination("/sub/chatroom/" + room_id);
		messagingTemplate.convertAndSend(
				"/sub/chatroom/" + room_id, "Disconnect", accessor.getMessageHeaders());
	}

	public void sendEnter(ChatDto dto, SimpMessageHeaderAccessor headerAccessor)
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

	public void chat(ChatDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Long room_id = dto.getChatroom_id();
		String session_id = headerAccessor.getSessionId();
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

		redisPublisher.publish(dto);

	}

	public void exit(ChatDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Long room_id = dto.getChatroom_id();
		String session_id = headerAccessor.getSessionId();
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

		chatroom.setActiveSessions(activeSessions);
		chatroomRepository.save(chatroom);
		disconnectSession(room_id, session_id);
		redisPublisher.publish(dto);

		if (nCount < 2) {

			ChatDto nDto = new ChatDto();
			nDto.setChatroom_id(chatroom.getId());
			nDto.setChatType(ChatType.NOTIFY);
			nDto.setMessage("해당 채팅방은 한명만 남은 채팅방입니다!");
			nDto.setSender(activeSessions.get(session_id));
			log.info(activeSessions.get(session_id));
			redisPublisher.publish(nDto);
		}
	}
}
