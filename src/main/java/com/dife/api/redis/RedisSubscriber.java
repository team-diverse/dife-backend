package com.dife.api.redis;

import com.dife.api.model.ChatType;
import com.dife.api.model.dto.ChatDto;
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
			ChatDto dto = objectMapper.readValue(publishMessage, ChatDto.class);

			if (dto.getChatType() == ChatType.ENTER) {
				messagingTemplate.convertAndSend(
						"/sub/chatroom/" + dto.getChatroom_id(), dto.getSender() + "님이 입장하셨습니다!");
			}
			if (dto.getChatType() == ChatType.CHAT) {
				messagingTemplate.convertAndSend("/sub/chatroom/" + dto.getChatroom_id(), dto.getMessage());
			}
			if (dto.getChatType() == ChatType.EXIT) {
				messagingTemplate.convertAndSend(
						"/sub/chatroom/" + dto.getChatroom_id(), dto.getSender() + "님이 퇴장하셨습니다!");
			}
			if (dto.getChatType() == ChatType.NOTIFY) {
				messagingTemplate.convertAndSend(
						"/sub/chatroom/" + dto.getChatroom_id(), "해당 채팅방은 한명만 남은 채팅방입니다!");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
