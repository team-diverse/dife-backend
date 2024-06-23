package com.dife.api.redis;

import com.dife.api.model.dto.ChatRedisDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisPublisher {
	private final RedisTemplate<String, String> redisTemplate;
	private final ChannelTopic topic;
	private final ObjectMapper objectMapper;

	public void publish(ChatRedisDto dto) throws JsonProcessingException {
		String jsonMessage = objectMapper.writeValueAsString(dto);
		redisTemplate.convertAndSend(topic.getTopic(), jsonMessage);
	}
}
