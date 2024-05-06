package com.dife.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

	@Override
	public void postSend(Message message, MessageChannel channel, boolean sent) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();

		switch ((accessor.getCommand())) {
			case CONNECT:
				log.info("세션 들어옴 => {} ", sessionId);
				break;

			case DISCONNECT:
				log.info("세션 끊음 => {} ", sessionId);
				break;

			default:
				break;
		}
	}
}
