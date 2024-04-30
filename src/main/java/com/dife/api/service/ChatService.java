package com.dife.api.service;

import com.dife.api.model.Chatroom;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

	private final ObjectMapper objectMapper;
	private Map<Long, Chatroom> chatrooms;

	@PostConstruct
	private void init() {
		chatrooms = new LinkedHashMap<>();
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
