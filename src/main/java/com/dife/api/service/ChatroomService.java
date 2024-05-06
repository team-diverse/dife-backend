package com.dife.api.service;

import com.dife.api.exception.ChatroomCountException;
import com.dife.api.exception.ChatroomDuplicateException;
import com.dife.api.exception.ChatroomException;
import com.dife.api.model.*;
import com.dife.api.model.dto.GroupChatroomRequestDto;
import com.dife.api.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatroomService {

	@Autowired private final ChatroomRepository chatroomRepository;

	public Chatroom createGroupChatroom(GroupChatroomRequestDto dto) {

		String name = dto.getName();
		String description = dto.getDescription();
		Integer max_count = dto.getMax_count();
		Integer min_count = dto.getMin_count();

		Chatroom chatroom = new Chatroom();
		if (chatroomRepository.existsByName(name)) {
			throw new ChatroomDuplicateException();
		}
		if (name == null || name.isEmpty()) {
			throw new ChatroomException("채팅방 이름은 필수사항입니다.");
		}
		chatroom.setName(name);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();

		if (min_count == null || max_count == null) {
			throw new ChatroomException("채팅방 인원 설정은 필수사항입니다.");
		}

		if (max_count > 30 || min_count < 3) {
			throw new ChatroomCountException();
		}
		setting.setMax_count(max_count);
		setting.setMin_count(min_count);

		if (description == null || description.isEmpty()) {
			throw new ChatroomException("채팅방 이름은 필수사항입니다.");
		}
		setting.setDescription(description);

		chatroom.setSetting(setting);

		chatroomRepository.save(chatroom);

		return chatroom;
	}

	public Boolean findChatroomById(Long id) {

		return chatroomRepository.existsById(id);
	}
}
