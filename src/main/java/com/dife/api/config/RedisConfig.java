package com.dife.api.config;

import com.dife.api.redis.RedisPublisher;
import com.dife.api.redis.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;

	@Value("${spring.data.redis.port}")
	private int redisPort;

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		RedisStandaloneConfiguration configuration =
				new RedisStandaloneConfiguration(redisHost, redisPort);
		return new LettuceConnectionFactory(configuration);
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}

	@Bean
	public ChannelTopic topic() {
		return new ChannelTopic("chatroom");
	}

	@Bean
	public RedisPublisher redisPublisher(ChannelTopic topic) {
		return new RedisPublisher(redisTemplate(), topic);
	}

	@Bean
	public MessageListenerAdapter messageListener(SimpMessagingTemplate messagingTemplate) {
		return new MessageListenerAdapter(new RedisSubscriber(redisTemplate(), messagingTemplate));
	}

	@Bean
	public RedisMessageListenerContainer redisContainer(SimpMessagingTemplate messagingTemplate) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(lettuceConnectionFactory());
		container.addMessageListener(messageListener(messagingTemplate), topic());
		return container;
	}
}
