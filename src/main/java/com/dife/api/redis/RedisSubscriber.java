package com.dife.api.redis;

import com.dife.api.model.dto.ChatRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
			String destination = "/sub/chatroom/" + dto.getChatroom_id();
			String nMessage = "";
			switch (dto.getChatType()) {
				case ENTER -> nMessage = dto.getSender() + "님이 입장하셨습니다!";
				case CHAT -> nMessage = dto.getMessage();
				case EXIT -> nMessage = dto.getSender() + "님이 퇴장하셨습니다!";
			}
			messagingTemplate.convertAndSend(destination, nMessage);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
