package com.dife.api.config;

import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.Member;
import com.dife.api.repository.MemberRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {

	private final JWTUtil jwtUtil;
	private final MemberRepository memberRepository;

	@SneakyThrows
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor =
				MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (StompCommand.CONNECT.equals(accessor.getCommand())
				|| StompCommand.SEND.equals(accessor.getCommand())) {
			String authToken = accessor.getFirstNativeHeader("authorization");
			String jwtToken = authToken.split(" ")[1];

			if (jwtToken == null || jwtUtil.isExpired(jwtToken)) {
				throw new AuthException("손상된 토큰입니다! 다시 로그인 하세요!");
			}

			Long id = jwtUtil.getId(jwtToken);

			Member member =
					memberRepository.findById(id).orElseThrow(() -> new AuthException("회원을 찾을 수 없습니다!"));

			if (member.getVerificationFile() != null && !member.getIsVerified())
				throw new AuthException("인증이 필요한 회원입니다!");
			accessor.addNativeHeader("memberEmail", member.getEmail());
		}
		return message;
	}
}
