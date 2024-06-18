package com.dife.api.redis;

import com.dife.api.model.dto.ChatRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

	private final RedisTemplate redisTemplate;
	private final SimpMessagingTemplate messagingTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			byte[] messageBody = message.getBody();
			String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(messageBody);

			ObjectMapper objectMapper = new ObjectMapper();
			ChatRequestDto dto = objectMapper.readValue(publishMessage, ChatRequestDto.class);
			String destination = "/sub/chatroom/" + dto.getChatroomId();

			Map<String, Object> enterMessage = new HashMap<>();
			enterMessage.put("username", dto.getUsername());
			enterMessage.put("message", dto.getMessage());
			enterMessage.put("created", LocalDateTime.now());
			messagingTemplate.convertAndSend(destination, enterMessage);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
