package com.dife.api.controller.chat;

import static org.junit.Assert.*;

import com.dife.api.config.LocalRedisConfig;
import com.dife.api.config.TestConfig;
import com.dife.api.model.*;
import com.dife.api.model.dto.ChatRequestDto;
import com.dife.api.model.dto.ChatResponseDto;
import com.dife.api.repository.ChatroomRepository;
import com.dife.api.service.ChatService;
import com.dife.api.service.ChatroomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
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

	@Autowired private ChatService chatService;

	@Autowired private ChatroomService chatroomService;

	@Autowired private ChatroomRepository chatroomRepository;

	@BeforeEach
	public void setUp() {
		url = "ws://localhost:" + port + "/ws";
		chatroom = chatroomRepository.getReferenceById(1L);
	}

	public WebSocketStompClient getStompClient() {

		WebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		return stompClient;
	}

	@Test
	void testConnectWebSocket() {

		Assertions.assertDoesNotThrow(
				() ->
						getStompClient()
								.connect(url, new StompSessionHandlerAdapter() {})
								.get(2, TimeUnit.SECONDS));
	}

	@Test
	void testSubscribeWebSocket() throws Exception {

		CompletableFuture<StompHeaders> completableFuture1 = new CompletableFuture<>();
		StompSession stompSession1 =
				getStompClient()
						.connect(
								url,
								new StompSessionHandlerAdapter() {
									@Override
									public void handleFrame(StompHeaders headers, Object payload) {
										completableFuture1.complete(headers);
									}
								})
						.get(2, TimeUnit.SECONDS);

		CompletableFuture<StompHeaders> completableFuture2 = new CompletableFuture<>();

		StompSession stompSession2 =
				getStompClient()
						.connect(
								url,
								new StompSessionHandlerAdapter() {
									@Override
									public void handleFrame(StompHeaders headers, Object payload) {
										completableFuture2.complete(headers);
									}
								})
						.get(2, TimeUnit.SECONDS);
		stompSession1.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});
		stompSession2.subscribe(
				"/sub/chatroom/" + +chatroom.getId(), new StompSessionHandlerAdapter() {});

		Assertions.assertThrows(
				TimeoutException.class, () -> completableFuture1.get(10, TimeUnit.SECONDS));
		Assertions.assertThrows(
				TimeoutException.class, () -> completableFuture2.get(10, TimeUnit.SECONDS));
	}

	@Test
	void testChatWebSocketAndGetChatroomChats() throws Exception {

		Set<Member> memberSet = chatroom.getMembers();
		List<Member> memberList = new ArrayList<>(memberSet);

		member1 = memberList.get(0);
		member2 = memberList.get(1);

		CompletableFuture<StompHeaders> completableFuture1 = new CompletableFuture<>();
		StompSession stompSession1 =
				getStompClient()
						.connect(
								url,
								new StompSessionHandlerAdapter() {

									@Override
									public void handleFrame(StompHeaders headers, Object payload) {
										completableFuture1.complete(headers);
									}
								})
						.get(60, TimeUnit.SECONDS);

		CompletableFuture<StompHeaders> completableFuture2 = new CompletableFuture<>();

		StompSession stompSession2 =
				getStompClient()
						.connect(
								url,
								new StompSessionHandlerAdapter() {

									@Override
									public void handleFrame(StompHeaders headers, Object payload) {
										completableFuture2.complete(headers);
									}
								})
						.get(60, TimeUnit.SECONDS);
		stompSession1.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});
		stompSession2.subscribe(
				"/sub/chatroom/" + chatroom.getId(), new StompSessionHandlerAdapter() {});

		for (int i = 0; i < 100; i++) {
			ChatRequestDto chatMessage = createChatDto(chatroom, member1);
			SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
			headerAccessor.setSessionId(stompSession1.getSessionId());
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

		Assertions.assertThrows(
				TimeoutException.class, () -> completableFuture1.get(10, TimeUnit.SECONDS));
		Assertions.assertThrows(
				TimeoutException.class, () -> completableFuture2.get(10, TimeUnit.SECONDS));
	}

	private ChatRequestDto createChatDto(Chatroom chatroom, Member member) {
		ChatRequestDto requestDto = new ChatRequestDto();
		requestDto.setChatType(ChatType.CHAT);
		requestDto.setChatroomId(chatroom.getId());
		requestDto.setMemberId(member.getId());
		requestDto.setMessage("채팅 테스트를 해볼게");

		return requestDto;
	}
}
