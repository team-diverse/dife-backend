package com.dife.api.controller.chat;

import static org.junit.Assert.*;

import com.dife.api.config.LocalRedisConfig;
import com.dife.api.config.TestConfig;
import com.dife.api.model.*;
import com.dife.api.model.dto.ChatRequestDto;
import com.dife.api.model.dto.ChatResponseDto;
import com.dife.api.model.dto.LoginDto;
import com.dife.api.model.dto.LoginSuccessDto;
import com.dife.api.repository.ChatroomRepository;
import com.dife.api.service.ChatService;
import com.dife.api.service.ChatroomService;
import com.dife.api.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@ExtendWith(LocalRedisConfig.class)
@Testcontainers
@Transactional
@ActiveProfiles("test")
public class WebSocketChatTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ObjectMapper objectMapper = new ObjectMapper();
	@LocalServerPort private int port;
	private String url;

	private Member member1;
	private Member member2;
	private Chatroom chatroom;

	private String testMember1Token;
	private String testMember2Token;

	private StompHeaders testMember1Header;
	private StompHeaders testMember2Header;

	@Autowired private ChatService chatService;
	@Autowired private MemberService memberService;

	@Autowired private ChatroomService chatroomService;

	@Autowired private ChatroomRepository chatroomRepository;

	@BeforeEach
	public void setUp() {
		url = "ws://localhost:" + port + "/ws";
		chatroom = chatroomRepository.getReferenceById(1L);

		Set<Member> memberSet = chatroom.getMembers();
		List<Member> memberList = new ArrayList<>(memberSet);

		member1 = memberList.get(0);
		member2 = memberList.get(1);

		testMember1Token = createTestToken(member1);
		testMember2Token = createTestToken(member2);

		testMember1Header = createStompHeaders(testMember1Token);
		testMember2Header = createStompHeaders(testMember2Token);
	}

	public WebSocketStompClient getStompClient() {

		WebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		return stompClient;
	}

	@Test
	void testConnectWebSocket() {

		WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();

		Assertions.assertDoesNotThrow(
				() ->
						getStompClient()
								.connect(
										url,
										webSocketHttpHeaders,
										testMember1Header,
										new StompSessionHandlerAdapter() {})
								.get(2, TimeUnit.SECONDS));
	}

	@Test
	void testSubscribeWebSocket() throws Exception {

		WebSocketHttpHeaders webSocketHttpHeaders1 = new WebSocketHttpHeaders();

		StompSession stompSession1 =
				getStompClient()
						.connect(
								url, webSocketHttpHeaders1, testMember1Header, new StompSessionHandlerAdapter() {})
						.get(2, TimeUnit.SECONDS);

		WebSocketHttpHeaders webSocketHttpHeaders2 = new WebSocketHttpHeaders();

		StompSession stompSession2 =
				getStompClient()
						.connect(
								url, webSocketHttpHeaders2, testMember2Header, new StompSessionHandlerAdapter() {})
						.get(2, TimeUnit.SECONDS);
		stompSession1.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});
		stompSession2.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});
	}

	@Test
	void testChatWebSocketAuthorization() throws Exception {

		WebSocketHttpHeaders webSocketHttpHeaders1 = new WebSocketHttpHeaders();

		StompSession stompSession1 =
				getStompClient()
						.connect(
								url, webSocketHttpHeaders1, testMember1Header, new StompSessionHandlerAdapter() {})
						.get(60, TimeUnit.SECONDS);

		WebSocketHttpHeaders webSocketHttpHeaders2 = new WebSocketHttpHeaders();

		StompSession stompSession2 =
				getStompClient()
						.connect(
								url, webSocketHttpHeaders2, testMember2Header, new StompSessionHandlerAdapter() {})
						.get(60, TimeUnit.SECONDS);
		stompSession1.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});

		stompSession2.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});

		ObjectMapper objectMapper = new ObjectMapper();
		String messageAsJson = objectMapper.writeValueAsString(createChatDto(chatroom));

		testMember1Header.add("destination", "/pub/chatroom/chat");
		stompSession1.send(testMember1Header, messageAsJson);
	}

	@Test
	void testChatWebSocketAndGetChatroomChats() throws Exception {

		WebSocketHttpHeaders webSocketHttpHeaders1 = new WebSocketHttpHeaders();

		StompSession stompSession1 =
				getStompClient()
						.connect(
								url, webSocketHttpHeaders1, testMember1Header, new StompSessionHandlerAdapter() {})
						.get(60, TimeUnit.SECONDS);
		WebSocketHttpHeaders webSocketHttpHeaders2 = new WebSocketHttpHeaders();

		StompSession stompSession2 =
				getStompClient()
						.connect(
								url, webSocketHttpHeaders2, testMember2Header, new StompSessionHandlerAdapter() {})
						.get(60, TimeUnit.SECONDS);
		stompSession1.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});
		stompSession2.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});

		for (int i = 0; i < 100; i++) {
			ChatRequestDto chatMessage = createChatDto(chatroom);
			SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
			headerAccessor.setSessionId(stompSession1.getSessionId());
			headerAccessor.setNativeHeader("memberEmail", member1.getEmail());
			headerAccessor.setDestination("/pub/chatroom/chat");

			try {
				chatService.sendMessage(chatMessage, headerAccessor);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
		List<ChatResponseDto> chats = chatroomService.getChats(chatroom.getId(), member2.getEmail());

		for (ChatResponseDto chat : chats) {
			String jsonChat = objectMapper.writeValueAsString(chat);
			System.out.println("===================");
			System.out.println("SAVED CHAT: " + jsonChat);
		}
	}

	private ChatRequestDto createChatDto(Chatroom chatroom) {
		ChatRequestDto requestDto = new ChatRequestDto();
		requestDto.setChatType(ChatType.CHAT);
		requestDto.setChatroomId(chatroom.getId());
		requestDto.setMessage("채팅 테스트를 해볼게");

		return requestDto;
	}

	private String createTestToken(Member member) {
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail(member.getEmail());
		loginDto.setPassword("password");

		LoginSuccessDto loginSuccessDto = memberService.login(loginDto).getBody();
		return loginSuccessDto.getAccessToken();
	}

	private StompHeaders createStompHeaders(String testToken) {
		StompHeaders memberHeaders = new StompHeaders();
		memberHeaders.add("authorization", "Bearer " + testToken);

		return memberHeaders;
	}
}
