package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.FileDto;
import com.dife.api.repository.ChatroomRepository;
import com.dife.api.repository.GroupPurposesRepository;
import com.dife.api.repository.LanguageRepository;
import com.dife.api.repository.TagRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatroomService {

	@Autowired private final ChatroomRepository chatroomRepository;
	private final TagRepository tagRepository;
	private final LanguageRepository languageRepository;
	private final GroupPurposesRepository groupPurposesRepository;

	private final FileService fileService;

	public Chatroom createGroupChatroom(String name, String description, MultipartFile profile_img) {

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

		if (description == null || description.isEmpty()) {
			throw new ChatroomException("채팅방 한줄소개는 필수사항입니다.");
		}
		setting.setDescription(description);

		if (profile_img != null && !profile_img.isEmpty()) {
			FileDto profileImgPath = fileService.upload(profile_img);
			setting.setProfile_img_name(profileImgPath.getName());
		} else {
			setting.setProfile_img_name(null);
		}

		chatroom.setSetting(setting);

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

	public Chatroom registerDetail(
			Set<String> tags,
			Integer min_count,
			Integer max_count,
			Set<String> languages,
			Set<String> purposes,
			Boolean is_public,
			String password,
			Long id) {
		Chatroom chatroom = getChatroom(id);

		ChatroomSetting setting = chatroom.getSetting();

		Set<Tag> myTags = new HashSet<>();
		if (tags == null || tags.isEmpty()) {
			throw new ChatroomException("채팅방 태그는 필수 입력사항입니다!");
		}
		for (String tag : tags) {
			Tag nTag = new Tag();
			nTag.setCrsetting(setting);
			nTag.setName(tag);
			tagRepository.save(nTag);
			myTags.add(nTag);
		}
		setting.setTags(myTags);

		if (min_count == null || max_count == null) {
			throw new ChatroomException("채팅방 인원수는 필수 입력사항입니다!");
		}
		if (min_count < 3 || min_count > 30 || max_count < 3 || max_count > 30) {
			throw new ChatroomException("채팅방 인원수 제한은 3~30명 입니다!");
		}
		setting.setMin_count(min_count);
		setting.setMax_count(max_count);

		Set<Language> myLanguages = new HashSet<>();
		if (languages == null || languages.isEmpty()) {
			throw new ChatroomException("채팅방 번역은 필수 입력사항입니다!");
		}
		for (String language : languages) {
			Language nLanguage = new Language();
			nLanguage.setName(language);
			nLanguage.setCrsetting(setting);
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
			nPurpose.setCrsetting(setting);
			groupPurposesRepository.save(nPurpose);
			myPurposes.add(nPurpose);
		}
		setting.setPurposes(myPurposes);
		if (is_public == null) {
			throw new ChatroomException("공개/비공개 여부는 필수 입력사항입니다!");
		}
		setting.setIs_public(is_public);

		if (password == null || !password.matches("^[0-9]{5}$")) {
			throw new ChatroomException("비밀번호는 5자리 숫자여야 합니다!");
		}
		setting.setPassword(password);

		chatroomRepository.save(chatroom);
		return chatroom;
	}
}
