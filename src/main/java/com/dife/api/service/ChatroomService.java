package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.ChatScraplistDto;
import com.dife.api.model.dto.ChatlistDto;
import com.dife.api.repository.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
	private final TagRepository tagRepository;
	private final LanguageRepository languageRepository;
	private final GroupPurposesRepository groupPurposesRepository;
	private final ChatRepository chatRepository;
	private final ChatScrapRepository chatScrapRepository;

	private final FileService fileService;

	public Chatroom createChatroom(String name, String description, ChatroomType type) {
		if (type == ChatroomType.GROUP) {
			return createGroupChatroom(name, description);
		} else {
			return createSingleChatroom();
		}
	}

	public Chatroom createGroupChatroom(String name, String description) {

		Chatroom chatroom = new Chatroom();
		if (chatroomRepository.existsByName(name)) {
			throw new ChatroomDuplicateException();
		}
		if (name == null || name.isEmpty()) {
			throw new ChatroomException("채팅방 이름은 필수사항입니다.");
		}
		String trimmedName = name.trim();
		chatroom.setName(trimmedName);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();

		if (description == null || description.isEmpty()) {
			throw new ChatroomException("채팅방 한줄소개는 필수사항입니다.");
		}

		if (description.length() > 60) {
			throw new ChatroomException("채팅방 한줄소개는 60자 이내입니다.");
		}

		String trimmedDescription = description.trim();
		if (trimmedDescription.isEmpty()) {
			throw new ChatroomException("유효하지 않은 한줄소개입니다. 공백만 존재하는 한줄 소개는 허용되지 않습니다.");
		}

		setting.setDescription(description);

		chatroom.setChatroom_setting(setting);

		chatroomRepository.save(chatroom);

		return chatroom;
	}

	public Chatroom createSingleChatroom() {
		Chatroom chatroom = new Chatroom();
		ChatroomSetting setting = new ChatroomSetting();
		chatroom.setChatroomType(ChatroomType.SINGLE);
		setting.setMax_count(2);
		chatroom.setChatroom_setting(setting);
		chatroomRepository.save(chatroom);

		return chatroom;
	}

	public Boolean findChatroomById(Long id) {

		return chatroomRepository.existsById(id);
	}

	public Chatroom getChatroom(Long id) {
		Chatroom chatroom = chatroomRepository.findById(id).orElseThrow(ChatroomNotFoundException::new);
		return chatroom;
	}

	public List<ChatlistDto> getChats(Long id) {

		List<Chat> chats = chatRepository.findChatsByChatroomId(id);

		return chats.stream().map(ChatlistDto::new).collect(Collectors.toList());
	}

	public List<ChatScraplistDto> getScraps(Long id) {

		List<ChatScrap> scraps = chatScrapRepository.findScrapsByChatroomId(id);

		return scraps.stream().map(ChatScraplistDto::new).collect(Collectors.toList());
	}

	public Chatroom registerDetail(
			Set<String> tags,
			Integer max_count,
			Set<String> languages,
			Set<String> purposes,
			Boolean is_public,
			String password,
			Long id) {
		Chatroom chatroom = getChatroom(id);

		ChatroomSetting setting = chatroom.getChatroom_setting();

		Set<Tag> myTags = new HashSet<>();
		if (tags == null || tags.isEmpty()) {
			throw new ChatroomException("채팅방 태그는 필수 입력사항입니다!");
		}
		for (String tag : tags) {
			Tag nTag = new Tag();
			nTag.setChatroom_setting(setting);
			nTag.setName(tag);
			tagRepository.save(nTag);
			myTags.add(nTag);
		}
		setting.setTags(myTags);

		if (max_count > 30) {
			throw new ChatroomException("채팅방 인원수 제한은 3~30명 입니다!");
		}
		setting.setMax_count(max_count);

		Set<Language> myLanguages = new HashSet<>();
		if (languages == null || languages.isEmpty()) {
			throw new ChatroomException("채팅방 번역 언어 선택은 필수 입력사항입니다!");
		}
		for (String language : languages) {
			Language nLanguage = new Language();
			nLanguage.setName(language);
			nLanguage.setChatroom_setting(setting);
			languageRepository.save(nLanguage);
			myLanguages.add(nLanguage);
		}
		setting.setLanguages(myLanguages);

		Set<GroupPurpose> myPurposes = new HashSet<>();
		if (purposes == null || purposes.isEmpty()) {
			throw new ChatroomException("채팅방 목적은 필수 입력사항입니다!");
		}
		for (String purpose : purposes) {
			GroupPurpose nPurpose = new GroupPurpose();
			nPurpose.setName(purpose);
			nPurpose.setChatroom_setting(setting);
			groupPurposesRepository.save(nPurpose);
			myPurposes.add(nPurpose);
		}
		setting.setPurposes(myPurposes);
		setting.setIs_public(is_public);

		if (!is_public) {
			if (password == null || !password.matches("^[0-9]{5}$")) {
				throw new ChatroomException("비밀번호는 5자리 숫자여야 합니다!");
			}
			setting.setPassword(password);
		}

		chatroomRepository.save(chatroom);
		return chatroom;
	}

	public Boolean isFull(Chatroom chatroom) {
		ChatroomSetting setting = chatroom.getChatroom_setting();
		Integer maxCount = setting.getMax_count();
		Integer nCount = setting.getCount();
		return nCount >= maxCount;
	}

	public Boolean isWrongPassword(Chatroom chatroom, String given_password) {
		ChatroomSetting setting = chatroom.getChatroom_setting();
		String password = setting.getPassword();

		return !password.equals(given_password);
	}
}
