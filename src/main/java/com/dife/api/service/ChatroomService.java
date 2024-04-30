package com.dife.api.service;

import com.dife.api.exception.ChatroomCountException;
import com.dife.api.exception.ChatroomDuplicateException;
import com.dife.api.exception.ChatroomException;
import com.dife.api.model.*;
import com.dife.api.model.dto.ChatDto;
import com.dife.api.model.dto.GroupChatroomRequestDto;
import com.dife.api.repository.ChatroomRepository;
import com.dife.api.repository.WebSocketSessionRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatroomService {

	@Autowired private final ChatroomRepository chatroomRepository;
	private final WebSocketSessionRepository webSocketSessionRepository;
	private final Map<String, WebSocketSession> webSocketSessionMap = new HashMap<>();

	public Chatroom createGroupChatroom(GroupChatroomRequestDto dto) {

		String name = dto.getName();
		String description = dto.getDescription();
		Integer max_count = dto.getMax_count();
		Integer min_count = dto.getMin_count();

		Chatroom chatroom = new Chatroom();
		if (chatroomRepository.existsByName(name)) {
			throw new ChatroomDuplicateException();
		}
		if (name == null || name.isEmpty()) {
			throw new ChatroomException("채팅방 이름은 필수사항입니다.");
		}
		chatroom.setName(name);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();

		if (min_count == null || max_count == null) {
			throw new ChatroomException("채팅방 인원 설정은 필수사항입니다.");
		}

		if (max_count > 30 || min_count < 3) {
			throw new ChatroomCountException();
		}
		setting.setMax_count(max_count);
		setting.setMin_count(min_count);

		if (description == null || description.isEmpty()) {
			throw new ChatroomException("채팅방 이름은 필수사항입니다.");
		}
		setting.setDescription(description);

		chatroom.setSetting(setting);

		chatroomRepository.save(chatroom);

		return chatroom;
	}

	public Chatroom findChatroomById(Long id, WebSocketSession session) {

		Optional<Chatroom> optionalChatroom = chatroomRepository.findById(id);
		if (optionalChatroom.isPresent()) {
			return optionalChatroom.get();
		}
		try {
			log.warn("존재하지 않는 채팅방 입니다!");
			session.sendMessage(new TextMessage("존재하지 않는 채팅방입니다. 세션을 종료하겠습니다."));
			session.close();
		} catch (IOException e) {
			log.error("세션 종료 중 에러 발생", e);
		}
		return null;
	}

	public void handleActions(
			WebSocketSession session, ChatDto chatDto, ChatService chatService, Chatroom chatroom) {
		if (chatroom == null) {
			return;
		}
		if (chatDto.getChatType().equals(ChatType.ENTER)) {
			String sessionId = session.getId();
			WebSocketSessionEntity sessionEntity = new WebSocketSessionEntity();
			sessionEntity.setSessionId(sessionId);
			sessionEntity.setChatroom(chatroom);
			webSocketSessionRepository.save(sessionEntity);
			webSocketSessionMap.put(sessionId, session);
			chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다!");
		}

		sendMessage(chatDto, chatService);
	}

	public <T> void sendMessage(T message, ChatService chatService) {
		for (WebSocketSession session : webSocketSessionMap.values()) {
			chatService.sendMessage(session, message);
		}
	}
}
