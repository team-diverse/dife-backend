package com.dife.api.service;

import com.dife.api.exception.ChatroomException;
import com.dife.api.exception.ChatroomNotFoundException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.handler.DisconnectHandler;
import com.dife.api.handler.NotificationHandler;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.redis.RedisPublisher;
import com.dife.api.repository.ChatRepository;
import com.dife.api.repository.ChatroomRepository;
import com.dife.api.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

	private final ChatroomRepository chatroomRepository;
	private final ChatRepository chatRepository;

	private final MemberRepository memberRepository;

	private final RedisPublisher redisPublisher;
	private final RedisLockChatServiceFacade chatServiceFacade;

	private final DisconnectHandler disconnectHandler;
	private final NotificationHandler notificationHandler;
	private final MemberService memberService;
	private final FileService fileService;
	private final BlockService blockService;
	private final NotificationService notificationService;
	private final ModelMapper modelMapper;

	public void sendMessage(ChatRequestDto dto, SimpMessageHeaderAccessor headerAccessor)
			throws JsonProcessingException {
		switch (dto.getChatType()) {
			case ENTER:
				enter(dto, headerAccessor);
				break;
			case CHAT:
				chat(dto);
				break;
			case FILE:
				try {
					file(dto);
				} catch (Exception e) {
					log.error("Error Message : {}", e.getMessage());
				}

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
		if (!disconnectHandler.canEnterChatroom(chatroom, member, sessionId, dto.getPassword())) {
			disconnectHandler.disconnect(chatroom.getId(), sessionId);
			return;
		}

		String username = member.getUsername();

		ChatroomSetting setting = chatroom.getChatroomSetting();
		chatServiceFacade.increase(chatroomId);

		chatroom.getMembers().add(member);
		chatroom.setChatroomSetting(setting);
		headerAccessor.getSessionAttributes().put("username", username);

		String enterMessage = username + "님이 입장하셨습니다!";
		Chat chat = saveChat(member, chatroom, enterMessage);

		Set<Member> chatroomMembers = chatroom.getMembers();
		chatroomMembers.add(member);

		for (Member chatroomMember : chatroomMembers) {
			String message =
					"WELCOME! 😊 " + chatroom.getName() + "방에 " + member.getUsername() + "님이 입장하셨습니다!";
			notificationService.addNotifications(
					chatroomMember, member, message, NotificationType.CHATROOM, chatroomId);
		}

		ChatRedisDto chatRedisDto = modelMapper.map(chat, ChatRedisDto.class);
		redisPublisher.publish(chatRedisDto);
		chatroomRepository.save(chatroom);
	}

	public void chat(ChatRequestDto dto) throws JsonProcessingException {

		Chatroom chatroom =
				chatroomRepository
						.findById(dto.getChatroomId())
						.orElseThrow(ChatroomNotFoundException::new);

		Member member = memberService.getMemberEntityById(dto.getMemberId());

		Set<Member> blockedMembers = blockService.getBlackSet(member);

		if (dto.getMessage().length() >= 300) {
			throw new ChatroomException("채팅 메시지는 300자 이하로 입력해주세요!");
		}

		Chat chat = saveChat(member, chatroom, dto.getMessage());
		Set<Member> chatroomMembers = chatroom.getMembers();

		for (Member chatroomMember : chatroomMembers) {
			if (blockedMembers.contains(chatroomMember)) return;

			if (!Objects.equals(chatroomMember.getId(), member.getId())) {
				List<NotificationToken> notificationTokens = chatroomMember.getNotificationTokens();

				for (NotificationToken notificationToken : notificationTokens) {
					Notification notification = new Notification();
					notification.setNotificationToken(notificationToken);
					notification.setType(NotificationType.CHATROOM);
					notification.setTypeId(chatroom.getId());
					notification.setChatMemberEmail(member.getEmail());
					notification.setCreated(LocalDateTime.now());

					String message = chat.getMessage();
					if (message.length() > 30) {
						message = message.substring(0, 30) + "...";
					}
					notification.setMessage(message);
					notificationToken.getNotifications().add(notification);

					notificationService.sendPushNotification(
							notificationToken.getPushToken(), notification.getCreated(), message);
				}
			}
		}

		ChatRedisDto chatRedisDto = modelMapper.map(chat, ChatRedisDto.class);
		redisPublisher.publish(chatRedisDto);
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

		ChatRedisDto chatRedisDto = modelMapper.map(chat, ChatRedisDto.class);
		redisPublisher.publish(chatRedisDto);
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

	public ChatResponseDto file(ChatRequestDto requestDto) throws JsonProcessingException {
		Member member =
				memberRepository
						.findById(requestDto.getMemberId())
						.orElseThrow(MemberNotFoundException::new);

		Chatroom chatroom =
				chatroomRepository
						.findById(requestDto.getChatroomId())
						.orElseThrow(ChatroomNotFoundException::new);
		if (!chatroom.getMembers().contains(member)) {
			throw new ChatroomException("채팅방 회원만이 채팅 파일을 보낼 수 있습니다!");
		}

		List<String> imgCodes = requestDto.getImgCode();

		Chat chat = new Chat();
		chat.setMember(member);
		chat.setChatroom(chatroom);
		List<String> awsS3ImageUrls = new ArrayList<>();

		for (String imgCode : imgCodes) {
			try {
				String[] strings = imgCode.split(",");
				String dataPrefix = strings[0];
				String base64Image = strings[1];
				String extension;

				if (dataPrefix.contains("jpeg")) {
					extension = "jpeg";
				} else if (dataPrefix.contains("png")) {
					extension = "png";
				} else {
					extension = "jpg";
				}

				byte[] imageBytes = Base64.getDecoder().decode(base64Image);

				String fileName = UUID.randomUUID().toString() + '.' + extension;
				String contentType = Files.probeContentType(new File(fileName).toPath());

				MultipartFile multipartFile = new Base64MultipartFile(imageBytes, fileName, contentType);
				FileDto fileDto = fileService.upload(multipartFile);
				awsS3ImageUrls.add(fileDto.getUrl());

			} catch (IOException ex) {
				log.error("IOException Error Message : {}", ex.getMessage());
				ex.printStackTrace();
			}
		}

		for (String awsS3ImageUrl : awsS3ImageUrls) {
			chat.getImgCode().add(awsS3ImageUrl);
		}

		chatRepository.save(chat);
		chatroomRepository.save(chatroom);

		Set<Member> chatroomMembers = chatroom.getMembers();

		for (Member chatroomMember : chatroomMembers) {
			if (!Objects.equals(chatroomMember.getId(), member.getId())) {
				List<NotificationToken> notificationTokens = chatroomMember.getNotificationTokens();

				for (NotificationToken notificationToken : notificationTokens) {
					Notification notification = new Notification();
					notification.setNotificationToken(notificationToken);
					notification.setType(NotificationType.CHATROOM);
					notification.setChatMemberEmail(member.getEmail());
					notification.setCreated(LocalDateTime.now());

					String message = member.getUsername() + "이 파일 메시지를 보냈습니다!";
					notification.setMessage(message);
					notificationToken.getNotifications().add(notification);

					notificationService.sendPushNotification(
							notificationToken.getPushToken(), notification.getCreated(), message);
				}
			}
		}

		ChatRedisDto chatRedisDto = modelMapper.map(chat, ChatRedisDto.class);
		redisPublisher.publish(chatRedisDto);

		return modelMapper.map(chat, ChatResponseDto.class);
	}
}
