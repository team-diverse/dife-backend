package com.dife.api.controller.chat;

import static org.assertj.core.api.Assertions.assertThat;

import com.dife.api.config.LocalRedisConfig;
import com.dife.api.config.TestConfig;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(LocalRedisConfig.class)
@Testcontainers
public class WebSocketConnectTest {

	@LocalServerPort private int port;

	@Test
	public void testWebSocketConnect() throws Exception {
		String url = "ws://localhost:" + port + "/ws";

		WebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());

		StompSession session =
				stompClient
						.connect(
								url,
								new StompSessionHandlerAdapter() {
									@Override
									public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
										System.out.println("Connected: " + connectedHeaders);
									}

									@Override
									public void handleFrame(StompHeaders headers, Object payload) {
										System.out.println("Received: " + payload);
									}

									@Override
									public Type getPayloadType(StompHeaders headers) {
										return String.class;
									}
								})
						.get(60, TimeUnit.SECONDS);

		assertThat(session.isConnected()).isTrue();

		session.disconnect();
	}
}
