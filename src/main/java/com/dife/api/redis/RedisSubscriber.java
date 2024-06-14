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

			switch (dto.getChatType()) {
				case ENTER:
					Map<String, Object> enterMessage = new HashMap<>();
					enterMessage.put("message", dto.getUsername() + "님이 입장하셨습니다!");
					messagingTemplate.convertAndSend(destination, enterMessage);
					break;
				case CHAT:
					Map<String, Object> chatMessage = new HashMap<>();
					chatMessage.put("username", dto.getUsername());
					chatMessage.put("message", dto.getMessage());
					chatMessage.put("created", LocalDateTime.now());
					messagingTemplate.convertAndSend(destination, chatMessage);
					break;
				case EXIT:
					Map<String, Object> exitMessage = new HashMap<>();
					exitMessage.put("message", dto.getUsername() + "님이 퇴장하셨습니다!");
					messagingTemplate.convertAndSend(destination, exitMessage);
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
