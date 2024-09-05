package com.dife.api;

import com.dife.api.config.LocalRedisConfig;
import com.dife.api.config.TestConfig;
import com.dife.api.model.Chatroom;
import com.dife.api.model.Member;
import com.dife.api.model.dto.ChatRedisDto;
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
		Chatroom chatroom = new Chatroom();
		Member member = new Member();
		ChatRedisDto dto = new ChatRedisDto();
		dto.setChatroom(chatroom);
		dto.setMember(member);
		dto.setMessage("Hello, Redis!");
		redisPublisher.publish(dto);
	}
}
