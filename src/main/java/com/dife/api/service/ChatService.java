package com.dife.api.service;

import com.dife.api.model.ChatType;
import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.dto.ChatDto;
import com.dife.api.model.dto.ChatEnterDto;
import com.dife.api.repository.ChatroomRepository;
import jakarta.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private Map<Long, Chatroom> chatrooms;
	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatroomService chatroomService;
	private final ChatroomRepository chatroomRepository;

	@PostConstruct
	private void init() {
		chatrooms = new LinkedHashMap<>();
	}

	public void sendMessage(Long room_id, ChatDto dto) {

	public void enter(Long room_id, String session_id, ChatEnterDto dto) {
		Boolean is_valid = chatroomService.findChatroomById(room_id);
		if (!is_valid) {
			disconnectSession(room_id, session_id);
			log.warn("유효한 채팅방이 아닙니다!");
			return;
		}
		if (dto.getChatType() != ChatType.ENTER) {
			disconnectSession(room_id, session_id);
			log.warn("해당 접근은 ENTER에 한해 유효합니다!");
			return;
		}

		Chatroom chatroom = chatroomService.getChatroom(room_id);
		if (chatroomService.isFull(chatroom)) {
			disconnectSession(room_id, session_id);
			log.warn("이미 해당 채팅방은 다 찬 채팅방입니다!");
			return;
		}
		if (chatroomService.isWrongPassword(chatroom, dto.getPassword())) {
			disconnectSession(room_id, session_id);
			log.warn("채팅방 비밀번호가 틀렸습니다! 다시 시도해주세요!");
			return;
		}

		Map<String, Boolean> activeSessions = chatroom.getActiveSessions();

		synchronized (activeSessions) {
			if (!activeSessions.containsKey(session_id)) {
				activeSessions.put(session_id, true);
				ChatroomSetting setting = chatroom.getChatroom_setting();
				Integer nCount = setting.getCount();
				nCount++;
				setting.setCount(nCount);
				messagingTemplate.convertAndSend(
						"/topic/chatroom/" + room_id, dto.getSender() + "님이 입장하셨습니다!");
			}
			if (dto.getChatType() == ChatType.CHAT) {
				messagingTemplate.convertAndSend("/topic/chatroom/" + room_id, dto.getMessage());
			}

		} else {
			throw new ChatroomNotFoundException();
		}
	}
}
