package com.dife.api.config;

import static org.springframework.messaging.simp.stomp.StompCommand.*;

import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.Chatroom;
import com.dife.api.model.Member;
import com.dife.api.model.dto.CustomUserDetails;
import com.dife.api.repository.MemberRepository;
import com.dife.api.service.ChatroomService;
import com.dife.api.service.MemberService;
import jakarta.security.auth.message.AuthException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

	private final JWTUtil jwtUtil;
	private final MemberRepository memberRepository;
	private final MemberService memberService;
	private final ChatroomService chatroomService;

	@SneakyThrows
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor =
				MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		if (accessor.getCommand().equals(StompCommand.CONNECT)) {
			Set<StompCommand> jwtExpiredCheckCommands = Set.of(CONNECT, SEND, MESSAGE, SUBSCRIBE);
			StompCommand currentCommand = accessor.getCommand();

			if (jwtExpiredCheckCommands.contains(currentCommand)) {
				String authToken = accessor.getFirstNativeHeader("authorization");
				String jwtToken = extractJwtToken(authToken);

				if (!isValidJwtToken(jwtToken)) {
					throw new AuthException("손상된 토큰입니다! 다시 로그인 하세요!");
				}

				Long memberId = jwtUtil.getId(jwtToken);
				Member member = memberService.getMemberEntityById(memberId);
				if (!memberService.isValidMember(member)) {
					throw new AuthException("인증이 필요한 회원입니다!");
				}
				if (currentCommand.equals(CONNECT)) {
					setAuthentication(member, accessor);
				}

				String destination = accessor.getDestination();

				if (currentCommand.equals(SUBSCRIBE)) {
					if (!isValidSubscriptionDestination(destination)) {
						throw new AuthException(
								"Unauthorized subscription to " + destination + ". Only chatrooms are allowed.");
					}
					Long chatroomId = parseChatroomIdFromDestination(destination);
					Chatroom chatroom = chatroomService.getChatroomById(chatroomId);
					if (!isMemberAllowedToSubscribe(member, chatroom)) {
						throw new AuthException("채팅방 메시지 Subscribe 권한이 없습니다.!");
					}
				}
			}
		}
		return message;
	}

	private boolean isValidSubscriptionDestination(String destination) {
		return destination != null && destination.startsWith("/sub/chatroom/");
	}

	private boolean isMemberAllowedToSubscribe(Member member, Chatroom chatroom) {
		return chatroomService.isMemberInChatroom(member, chatroom);
	}

	private Long parseChatroomIdFromDestination(String destination) {
		return Long.parseLong(destination.split("/")[3]);
	}

	private String extractJwtToken(String authToken) {
		return authToken.split(" ")[1];
	}

	private boolean isValidJwtToken(String jwtToken) {
		return jwtToken != null && !jwtUtil.isExpired(jwtToken);
	}

	private void setAuthentication(Member member, StompHeaderAccessor accessor) {
		CustomUserDetails customUserDetails = new CustomUserDetails(member);
		Authentication authentication =
				new UsernamePasswordAuthenticationToken(
						customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		accessor.setUser(authentication);
	}
}
