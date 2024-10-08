package com.dife.api.config;

import static org.springframework.messaging.simp.SimpMessageType.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class SecurityWebSocketConfig {
	@Bean
	AuthorizationManager<Message<?>> authorizationManager(
			MessageMatcherDelegatingAuthorizationManager.Builder messages) {
		return messages
				.simpTypeMatchers(CONNECT, UNSUBSCRIBE, DISCONNECT, HEARTBEAT)
				.permitAll()
				.simpMessageDestMatchers("/pub/**")
				.authenticated()
				.simpSubscribeDestMatchers("/sub/**")
				.authenticated()
				.simpTypeMatchers(MESSAGE, SUBSCRIBE)
				.authenticated()
				.anyMessage()
				.denyAll()
				.build();
	}

	@Bean("csrfChannelInterceptor")
	ChannelInterceptor csrfChannelInterceptor() {
		return new ChannelInterceptor() {};
	}
}
