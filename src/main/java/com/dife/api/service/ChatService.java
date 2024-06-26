package com.dife.api.service;

import com.dife.api.exception.ChatroomNotFoundException;
import com.dife.api.handler.DisconnectHandler;
import com.dife.api.handler.NotificationHandler;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.redis.RedisPublisher;
import com.dife.api.repository.ChatRepository;
import com.dife.api.repository.ChatroomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

	private final ChatroomRepository chatroomRepository;
	private final ChatRepository chatRepository;

	private final RedisPublisher redisPublisher;
	private final RedisLockChatServiceFacade chatServiceFacade;

	private final DisconnectHandler disconnectHandler;
	private final NotificationHandler notificationHandler;
	private final MemberService memberService;

	public void sendMessage(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {
		switch (dto.getChatType()) {
			case ENTER:
				enter(dto, headerAccessor);
				break;
			case CHAT:
				chat(dto);
				break;
			case EXIT:
				exit(dto, headerAccessor);
		}
	}

	public void enter(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Chatroom chatroom =
				chatroomRepository
						.findById(dto.getChatroomId())
						.orElseThrow(ChatroomNotFoundException::new);
		Long chatroomId = chatroom.getId();
		String sessionId = headerAccessor.getSessionId();

		Member member = memberService.getMemberEntityById(dto.getMemberId());
		if (!disconnectHandler.isEnterDisconnectChecked(chatroom, member, sessionId, dto.getPassword()))
			return;

		String username = member.getUsername();

		ChatroomSetting setting = chatroom.getChatroomSetting();
		chatServiceFacade.increase(chatroomId);

		chatroom.getMembers().add(member);
		chatroom.setChatroomSetting(setting);
		headerAccessor.getSessionAttributes().put("username", username);

		String enterMessage = username + "님이 입장하셨습니다!";
		Chat chat = saveChat(member, chatroom, enterMessage);
		updateChatRequestDto(dto, chat);
		redisPublisher.publish(dto);
		chatroomRepository.save(chatroom);
	}

	public void chat(ChatRequestDto dto) throws JsonProcessingException {

		Chatroom chatroom =
				chatroomRepository
						.findById(dto.getChatroomId())
						.orElseThrow(ChatroomNotFoundException::new);

		Member member = memberService.getMemberEntityById(dto.getMemberId());

		if (dto.getMessage().length() <= 300) {
			Chat chat = saveChat(member, chatroom, dto.getMessage());
			updateChatRequestDto(dto, chat);
			redisPublisher.publish(dto);
		}
	}

	public void exit(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {

		Chatroom chatroom =
				chatroomRepository
						.findById(dto.getChatroomId())
						.orElseThrow(ChatroomNotFoundException::new);
		ChatroomSetting setting = chatroom.getChatroomSetting();
		Member member = memberService.getMemberEntityById(dto.getMemberId());

		Long chatroomId = dto.getChatroomId();
		String sessionId = headerAccessor.getSessionId();
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		dto.setUsername(username);

		if (!disconnectHandler.isExitDisconnectChecked(chatroom, sessionId)) return;

		chatServiceFacade.decrease(chatroomId);

		chatroom.getMembers().remove(member);
		disconnectHandler.disconnect(chatroomId, sessionId);

		String exitMessage = username + "님이 퇴장하셨습니다!";
		Chat chat = saveChat(member, chatroom, exitMessage);
		updateChatRequestDto(dto, chat);

		dto.setMessage(exitMessage);
		redisPublisher.publish(dto);
		notificationHandler.isAlone(chatroom, sessionId);

		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);
	}

	public Chat saveChat(Member member, Chatroom chatroom, String message) {
		Chat chat = new Chat();
		chat.setMessage(message);
		chat.setChatroom(chatroom);
		chat.setMember(member);
		chat.setCreated(LocalDateTime.now());
		chatRepository.save(chat);
		return chat;
	}

	private void updateChatRequestDto(ChatRequestDto dto, Chat chat) {
		dto.setUsername(chat.getMember().getUsername());
		dto.setMessage(chat.getMessage());
		dto.setCreated(chat.getCreated());
	}
}
