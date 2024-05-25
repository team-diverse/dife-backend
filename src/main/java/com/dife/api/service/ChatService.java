package com.dife.api.service;

import com.dife.api.exception.ChatroomNotFoundException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.redis.RedisPublisher;
import com.dife.api.repository.ChatRepository;
import com.dife.api.repository.ChatroomRepository;
import com.dife.api.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatroomService chatroomService;
	private final ChatroomRepository chatroomRepository;
	private final RedisPublisher redisPublisher;
	private final ChatRepository chatRepository;
	private final MemberRepository memberRepository;

	@Autowired private final RedisLockChatServiceFacade chatServiceFacade;

	@Transactional
	public void sendMessage(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException, InterruptedException {
		switch (dto.getChatType()) {
			case ENTER:
				enter(dto, headerAccessor);
				break;
			case CHAT:
				chat(dto, headerAccessor);
				break;
			case EXIT:
				exit(dto, headerAccessor);
		}
	}

	public void disconnectSession(Long chatroomId, String sessionId) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		accessor.setSessionId(sessionId);
		accessor.setDestination("/sub/chatroom/" + chatroomId);
		messagingTemplate.convertAndSend(
				"/sub/chatroom/" + chatroomId, "Disconnect", accessor.getMessageHeaders());
	}

	public Chatroom validChatroom(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor) {
		Long chatroomId = dto.getChatroomId();
		String sessionId = headerAccessor.getSessionId();

		Boolean is_valid =
				(chatroomRepository.existsById(chatroomId)
						&& memberRepository.existsById(dto.getMemberId()));
		if (!is_valid) {
			disconnectSession(chatroomId, sessionId);
		}
		return chatroomRepository
				.findById(chatroomId)
				.orElseThrow(() -> new ChatroomNotFoundException());
	}

	public void enter(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Chatroom chatroom = validChatroom(dto, headerAccessor);
		Long chatroomId = chatroom.getId();
		String sessionId = headerAccessor.getSessionId();
		Boolean notValidGroupChatroom =
				(chatroom.getChatroomType() == ChatroomType.GROUP
						&& (!chatroom.getChatroomSetting().getIsPublic()
								&& chatroomService.isWrongPassword(chatroom, dto.getPassword())));

		Member member =
				memberRepository.findById(dto.getMemberId()).orElseThrow(MemberNotFoundException::new);
		String username = member.getUsername();

		ChatroomSetting setting = chatroom.getChatroomSetting();

		if (notValidGroupChatroom) {
			disconnectSession(chatroomId, sessionId);
			return;
		}

		if (setting.getCount() < setting.getMaxCount()) {
			chatServiceFacade.increase(chatroomId, sessionId);
		} else {
			disconnectSession(chatroomId, sessionId);
			return;
		}

		chatroom.setChatroomSetting(setting);
		headerAccessor.getSessionAttributes().put("username", username);

		dto.setUsername(member.getUsername());
		redisPublisher.publish(dto);
		chatroomRepository.save(chatroom);
	}

	public void chat(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Chatroom chatroom = validChatroom(dto, headerAccessor);
		String username = (String) headerAccessor.getSessionAttributes().get("username");

		if (dto.getMessage().length() <= 300) {
			Chat chat = new Chat();
			chat.setMessage(dto.getMessage());
			chat.setChatroom(chatroom);

			chatRepository.save(chat);
			dto.setUsername(username);
			redisPublisher.publish(dto);
		}
	}

	public void exit(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException, InterruptedException {

		Chatroom chatroom = validChatroom(dto, headerAccessor);
		ChatroomSetting setting = chatroom.getChatroomSetting();
		Long chatroom_id = dto.getChatroomId();
		String session_id = headerAccessor.getSessionId();
		String username = (String) headerAccessor.getSessionAttributes().get("username");

		if (setting.getCount() >= 1) {
			chatServiceFacade.decrease(chatroom_id, session_id);
		} else {
			disconnectSession(chatroom_id, session_id);
			return;
		}

		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);
		dto.setUsername(username);
		disconnectSession(chatroom_id, session_id);
		redisPublisher.publish(dto);

		if (setting.getCount() < 2) {

			StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
			accessor.setSessionId(session_id);
			accessor.setDestination("/sub/chatroom/" + chatroom_id);
			messagingTemplate.convertAndSend(
					"/sub/chatroom/" + chatroom_id, "해당 채팅방은 한 명만 남은 채팅방입니다!", accessor.getMessageHeaders());
		}
	}
}
