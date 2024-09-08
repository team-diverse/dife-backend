package com.dife.api.redis;

import com.dife.api.model.dto.ChatRedisDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private final ObjectMapper objectMapper;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			byte[] messageBody = message.getBody();
			String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(messageBody);

			ChatRedisDto dto = objectMapper.readValue(publishMessage, ChatRedisDto.class);

			String destination;
			if (dto.getGroupChatroom() != null)
				destination = "/sub/chatroom/" + dto.getGroupChatroom().getId();
			else destination = "/sub/chatroom/" + dto.getSingleChatroom().getId();

			Map<String, Object> propagateMessage = objectMapper.convertValue(dto, Map.class);
			messagingTemplate.convertAndSend(destination, propagateMessage);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
