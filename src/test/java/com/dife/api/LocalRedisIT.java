package com.dife.api;

import com.dife.api.config.LocalRedisConfig;
import com.dife.api.config.TestConfig;
import com.dife.api.model.ChatType;
import com.dife.api.model.dto.ChatRequestDto;
import com.dife.api.redis.RedisPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(LocalRedisConfig.class)
@Testcontainers
public class LocalRedisIT {

	@Autowired private RedisPublisher redisPublisher;

	@Test
	void test() throws Exception {
		ChatRequestDto chatRequestDto = new ChatRequestDto();
		chatRequestDto.setChatroomId(1L);
		chatRequestDto.setUsername("user1");
		chatRequestDto.setChatType(ChatType.CHAT);
		chatRequestDto.setMessage("Hello, Redis!");

		redisPublisher.publish(chatRequestDto);
	}
}
