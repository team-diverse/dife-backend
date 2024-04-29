package com.dife.api.service;

import com.dife.api.exception.ChatroomCountException;
import com.dife.api.exception.ChatroomDuplicateException;
import com.dife.api.exception.ChatroomException;
import com.dife.api.model.*;
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

	@Autowired private final ChatroomRepository chatRoomRepository;

	public Chatroom createGroupChatroom(
			String name, String description, String max_count, String min_count) {
		Chatroom chatroom = new Chatroom();
		if (chatRoomRepository.existsByName(name)) {
			throw new ChatroomDuplicateException();
		}
		if (name == null || name.isEmpty()) {
			throw new ChatroomException("채팅방 이름은 필수사항입니다.");
		}
		chatroom.setName(name);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();

		if (min_count == null || max_count == null || min_count.isEmpty() || max_count.isEmpty()) {
			throw new ChatroomException("채팅방 인원 설정은 필수사항입니다.");
		}

		int maxCount = Integer.parseInt(max_count);
		int minCount = Integer.parseInt(min_count);
		if (maxCount > 30 || minCount < 3) {
			throw new ChatroomCountException();
		}
		setting.setMax_count(maxCount);
		setting.setMin_count(minCount);

		if (description == null || description.isEmpty()) {
			throw new ChatroomException("채팅방 이름은 필수사항입니다.");
		}
		setting.setDescription(description);

		chatroom.setSetting(setting);

		chatRoomRepository.save(chatroom);

		return chatroom;
	}
}
