package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.*;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatroomService {

	private final ChatroomRepository chatroomRepository;
	private final TagRepository tagRepository;
	private final LanguageRepository languageRepository;
	private final GroupPurposesRepository groupPurposesRepository;
	private final MemberRepository memberRepository;
	private final ChatRepository chatRepository;

	private final ModelMapper modelMapper;

	@Autowired
	@Qualifier("chatroomModelMapper")
	private ModelMapper chatroomModelMapper;

	private final FileService fileService;

	public List<ChatroomResponseDto> getChatrooms(ChatroomType chatroomType, String memberEmail) {

		List<Chatroom> chatrooms;

		if (chatroomType == ChatroomType.GROUP)
			chatrooms = chatroomRepository.findAllByChatroomType(chatroomType);
		else {
			Member member =
					memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
			chatrooms = chatroomRepository.findAllByChatroomTypeAndMember(chatroomType, member);
		}
		return chatrooms.stream()
				.map(c -> modelMapper.map(c, ChatroomResponseDto.class))
				.collect(toList());
	}

	public ChatroomResponseDto createChatroom(ChatroomPostRequestDto requestDto, String memberEmail) {
		switch (requestDto.getChatroomType()) {
			case GROUP:
				return createGroupChatroom(requestDto, memberEmail);
			case SINGLE:
				return createSingleChatroom(requestDto, memberEmail);
		}
		throw new ChatroomException("유효한 채팅방 생성 접근이 아닙니다!");
	}

	public ChatroomResponseDto createGroupChatroom(
			ChatroomPostRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		String trimmedName = requestDto.getName().trim();
		if (chatroomRepository.existsByName(trimmedName)) throw new ChatroomDuplicateException();
		if (trimmedName.isEmpty()) throw new ChatroomException("채팅방 이름은 필수사항입니다.");

		Chatroom chatroom = new Chatroom();

		chatroom.getMembers().add(member);

		chatroom.setName(trimmedName);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();
		String trimmedDescription = requestDto.getDescription().trim();
		if (trimmedDescription.isEmpty())
			throw new ChatroomException("유효하지 않은 한줄소개입니다. 공백만 존재하는 한줄 소개는 허용되지 않습니다.");

		setting.setCount(setting.getCount() + 1);
		setting.setDescription(trimmedDescription);
		chatroom.setChatroomSetting(setting);

		chatroomRepository.save(chatroom);

		return chatroomModelMapper.map(chatroom, ChatroomResponseDto.class);
	}

	public ChatroomResponseDto registerDetail(
			GroupChatroomPutRequestDto requestDto, Long chatroomId, String memberEmail) {

		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		if (!chatroom.getMembers().contains(member)) throw new MemberException("수정 권한이 있는 회원이 아닙니다!");

		ChatroomSetting setting = chatroom.getChatroomSetting();

		Set<Tag> existingTags = tagRepository.findTagsByChatroomSetting(setting);
		Map<String, Tag> nameToTagMap =
				existingTags.stream().collect(Collectors.toMap(Tag::getName, Function.identity()));

		Set<Tag> updatedTags = new HashSet<>();

		for (String tagName : requestDto.getTags()) {
			if (nameToTagMap.containsKey(tagName)) {
				updatedTags.add(nameToTagMap.get(tagName));
			} else {
				Tag nTag = new Tag();
				nTag.setName(tagName);
				nTag.setChatroomSetting(setting);
				tagRepository.save(nTag);
				updatedTags.add(nTag);
			}
		}
		existingTags.stream()
				.filter(tag -> !requestDto.getTags().contains(tag.getName()))
				.forEach(tagRepository::delete);

		setting.setTags(updatedTags);

		if (requestDto.getMaxCount() > 30 || requestDto.getMaxCount() < 3)
			throw new ChatroomException("그룹 채팅방 인원은 3명 이상 30명 이하여야 합니다!");
		setting.setMaxCount(requestDto.getMaxCount());

		Set<Language> existingLanguages = languageRepository.findLanguagesByChatroomSetting(setting);
		Map<String, Language> nameToLanguageMap =
				existingLanguages.stream()
						.collect(Collectors.toMap(Language::getName, Function.identity()));

		Set<Language> updatedLanguages = new HashSet<>();

		for (String languageName : requestDto.getLanguages()) {
			if (nameToLanguageMap.containsKey(languageName)) {
				updatedLanguages.add(nameToLanguageMap.get(languageName));
			} else {
				Language nLanguage = new Language();
				nLanguage.setName(languageName);
				nLanguage.setChatroomSetting(setting);
				languageRepository.save(nLanguage);
				updatedLanguages.add(nLanguage);
			}
		}
		existingLanguages.stream()
				.filter(language -> !requestDto.getLanguages().contains(language.getName()))
				.forEach(languageRepository::delete);

		setting.setLanguages(updatedLanguages);

		Set<GroupPurpose> existingGroupPurposes =
				groupPurposesRepository.findGroupPurposesByChatroomSetting(setting);
		Map<String, GroupPurpose> nameToGroupPurposeMap =
				existingGroupPurposes.stream()
						.collect(Collectors.toMap(GroupPurpose::getName, Function.identity()));

		Set<GroupPurpose> updatedGroupPurposes = new HashSet<>();

		for (String groupPurposeName : requestDto.getPurposes()) {
			if (nameToGroupPurposeMap.containsKey(groupPurposeName)) {
				updatedGroupPurposes.add(nameToGroupPurposeMap.get(groupPurposeName));
			} else {
				GroupPurpose nPurpose = new GroupPurpose();
				nPurpose.setName(groupPurposeName);
				nPurpose.setChatroomSetting(setting);
				groupPurposesRepository.save(nPurpose);
				myGroupPurposes.add(nPurpose);
			}
		}
		existingGroupPurposes.stream()
				.filter(groupPurpose -> !requestDto.getPurposes().contains(groupPurpose.getName()))
				.forEach(groupPurposesRepository::delete);

		setting.setPurposes(updatedGroupPurposes);
		Boolean isPublic = requestDto.getIsPublic();
		setting.setIsPublic(isPublic);

		if (!isPublic) {
			setting.setPassword(requestDto.getPassword());
		}

		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);

		return chatroomModelMapper.map(chatroom, ChatroomResponseDto.class);
	}

	public ChatroomResponseDto createSingleChatroom(
			ChatroomPostRequestDto requestDto, String currentMemberEmail) {

		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member otherMember =
				memberRepository
						.findById(requestDto.getMemberId())
						.orElseThrow(MemberNotFoundException::new);

		if (chatroomRepository.existsSingleChatroomByMembers(
				currentMember, otherMember, ChatroomType.SINGLE))
			throw new SingleChatroomCreateDuplicateException();

		Chatroom chatroom = new Chatroom();
		ChatroomSetting setting = new ChatroomSetting();
		chatroom.setChatroomType(ChatroomType.SINGLE);
		Set<Member> memberSet = chatroom.getMembers();
		memberSet.add(currentMember);
		memberSet.add(otherMember);
		setting.setMaxCount(memberSet.size() > 1 ? 2 : 1);
		setting.setCount(memberSet.size());
		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);

		return chatroomModelMapper.map(chatroom, ChatroomResponseDto.class);
	}

	public ChatroomResponseDto getChatroom(Long id) {

		Chatroom chatroom = chatroomRepository.findById(id).orElseThrow(ChatroomNotFoundException::new);
		return chatroomModelMapper.map(chatroom, ChatroomResponseDto.class);
	}

	public List<ChatResponseDto> getChats(Long chatroomId, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);

		if (!chatroom.getMembers().contains(member)) {
			throw new ChatroomException("소속회원만이 채팅 불러올 수 있음");
		}
		List<Chat> chats = chatRepository.findChatsByChatroomId(chatroomId);

		return chats.stream().map(c -> modelMapper.map(c, ChatResponseDto.class)).collect(toList());
	}

	public ChatResponseDto getChat(Long chatroomId, Long chatId, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);

		if (!chatroom.getMembers().contains(member)) {
			throw new ChatroomException("소속회원만이 채팅 불러올 수 있음");
		}

		Chat chat =
				chatRepository
						.findByChatroomIdAndId(chatroomId, chatId)
						.orElseThrow(() -> new ChatroomException("존재하지 않는 채팅입니다!"));

		return modelMapper.map(chat, ChatResponseDto.class);
	}

	public void increase(Long chatroomId) {
		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);
		ChatroomSetting setting = chatroom.getChatroomSetting();
		if (setting.getCount() >= setting.getMaxCount()) {
			return;
		}
		setting.setCount(setting.getCount() + 1);
		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);
	}

	public void decrease(Long chatroomId) {
		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);
		ChatroomSetting setting = chatroom.getChatroomSetting();
		if (setting.getCount() < 1) return;
		setting.setCount(setting.getCount() - 1);
		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);
	}
}
