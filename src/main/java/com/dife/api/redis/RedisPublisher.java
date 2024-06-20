package com.dife.api.redis;

import com.dife.api.model.dto.ChatRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RedisPublisher {
	private RedisTemplate<String, String> redisTemplate;
	private ChannelTopic topic;

	public void publish(ChatRequestDto dto) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String jsonMessage = mapper.writeValueAsString(dto);

		redisTemplate.convertAndSend(topic.getTopic(), jsonMessage);
	}
}
