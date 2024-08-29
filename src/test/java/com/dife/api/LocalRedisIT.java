package com.dife.api;

import com.dife.api.config.LocalRedisConfig;
import com.dife.api.config.TestConfig;
import com.dife.api.model.Chatroom;
import com.dife.api.model.Member;
import com.dife.api.model.dto.ChatRedisDto;
import com.dife.api.model.dto.ChatroomResponseDto;
import com.dife.api.model.dto.MemberResponseDto;
import com.dife.api.redis.RedisPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

	@Autowired
	@Qualifier("memberModelMapper")
	private ModelMapper memberModelMapper;

	@Autowired
	@Qualifier("chatroomModelMapper")
	private ModelMapper chatroomModelMapper;

	@Test
	void test() throws Exception {
		Chatroom chatroom = new Chatroom();
		Member member = new Member();
		ChatRedisDto dto = new ChatRedisDto();
		dto.setChatroom(chatroomModelMapper.map(chatroom, ChatroomResponseDto.class));
		dto.setMember(memberModelMapper.map(member, MemberResponseDto.class));
		dto.setMessage("Hello, Redis!");
		redisPublisher.publish(dto);
	}
}
