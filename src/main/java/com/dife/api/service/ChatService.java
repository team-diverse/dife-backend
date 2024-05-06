package com.dife.api.service;

import com.dife.api.exception.ChatroomNotFoundException;
import com.dife.api.model.ChatType;
import com.dife.api.model.Chatroom;
import com.dife.api.model.dto.ChatDto;
import jakarta.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private Map<Long, Chatroom> chatrooms;
	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatroomService chatroomService;

	@PostConstruct
	private void init() {
		chatrooms = new LinkedHashMap<>();
	}

	public void sendMessage(Long room_id, ChatDto dto) {

		Boolean is_valid = chatroomService.findChatroomById(room_id);
		if (is_valid) {
			if (dto.getChatType() == ChatType.ENTER) {
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
