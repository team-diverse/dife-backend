package com.dife.api.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@AllArgsConstructor
public class RedisPublisher {
	private RedisTemplate<String, String> redisTemplate;
	private ChannelTopic topic;

	public void publish(String message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}
}
