package com.dife.api.config;

import com.dife.api.redis.RedisPublisher;
import com.dife.api.redis.RedisSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
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

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redisson() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
		return Redisson.create(config);
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
	public RedisPublisher redisPublisher(
			RedisTemplate<String, String> redisTemplate, ChannelTopic topic, ObjectMapper objectMapper) {
		return new RedisPublisher(redisTemplate, topic, objectMapper);
	}

	@Bean
	public MessageListenerAdapter messageListener(RedisSubscriber redisSubscriber) {
		return new MessageListenerAdapter(redisSubscriber);
	}

	@Bean
	public RedisSubscriber redisSubscriber(
			RedisTemplate<String, String> redisTemplate,
			SimpMessagingTemplate messagingTemplate,
			ObjectMapper objectMapper) {
		return new RedisSubscriber(redisTemplate, messagingTemplate, objectMapper);
	}

	@Bean
	public RedisMessageListenerContainer redisContainer(
			MessageListenerAdapter messageListenerAdapter, ChannelTopic topic) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(lettuceConnectionFactory());
		container.addMessageListener(messageListenerAdapter, topic);
		return container;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaTimeModule module = new JavaTimeModule();
		module.addSerializer(
				LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
		objectMapper.registerModule(module);
		return objectMapper;
	}
}
