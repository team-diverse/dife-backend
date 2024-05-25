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
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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

	private final SimpMessageSendingOperations messagingTemplate;
	private final ModelMapper modelMapper;

	@Autowired
	@Qualifier("chatroomModelMapper")
	private ModelMapper chatroomModelMapper;

	private final FileService fileService;

	public List<ChatroomResponseDto> getChatrooms(
			ChatroomTypeRequestDto requestDto, String memberEmail) {

		List<Chatroom> chatrooms;

		if (requestDto.getChatroomType() == ChatroomType.GROUP)
			chatrooms = chatroomRepository.findAllByChatroomType(requestDto.getChatroomType());
		else {
			Member member =
					memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
			chatrooms =
					chatroomRepository.findAllByChatroomTypeAndMember(requestDto.getChatroomType(), member);
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
		if (!chatroomRepository.existsByMemberAndId(member, chatroomId))
			throw new MemberException("수정 권한이 있는 회원이 아닙니다!");

		ChatroomSetting setting = chatroom.getChatroomSetting();

		Set<Tag> myTags = setting.getTags();
		for (String tagName : requestDto.getTags()) {
			if (!tagRepository.existsTagByNameAndChatroomSetting(tagName, setting)) {
				Tag nTag = new Tag();
				nTag.setName(tagName);
				nTag.setChatroom_setting(setting);
				tagRepository.save(nTag);
				myTags.add(nTag);
			}
		}
		setting.setTags(myTags);
		if (requestDto.getMaxCount() > 30 || requestDto.getMaxCount() < 3)
			throw new ChatroomException("그룹 채팅방 인원은 3명 이상 30명 이하여야 합니다!");
		setting.setMaxCount(requestDto.getMaxCount());

		Set<Language> myLanguages = setting.getLanguages();
		for (String languageName : requestDto.getLanguages()) {
			if (!languageRepository.existsLanguageByNameAndChatroomSetting(languageName, setting)) {
				Language nLanguage = new Language();
				nLanguage.setName(languageName);
				nLanguage.setChatroom_setting(setting);
				languageRepository.save(nLanguage);
				myLanguages.add(nLanguage);
			}
		}
		setting.setLanguages(myLanguages);

		Set<GroupPurpose> myGroupPurposes = setting.getPurposes();
		for (String groupPurposeName : requestDto.getPurposes()) {
			if (!groupPurposesRepository.existsGroupPurposeByNameAndChatroomSetting(
					groupPurposeName, setting)) {
				GroupPurpose nPurpose = new GroupPurpose();
				nPurpose.setName(groupPurposeName);
				nPurpose.setChatroom_setting(setting);
				groupPurposesRepository.save(nPurpose);
				myGroupPurposes.add(nPurpose);
			}
		}
		setting.setPurposes(myGroupPurposes);

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
		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);

		return chatroomModelMapper.map(chatroom, ChatroomResponseDto.class);
	}

	public ChatroomResponseDto getChatroom(Long id) {

		Chatroom chatroom = chatroomRepository.findById(id).orElseThrow(ChatroomNotFoundException::new);
		return chatroomModelMapper.map(chatroom, ChatroomResponseDto.class);
	}

	public List<ChatResponseDto> getChats(
			ChatsGetByChatroomRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		if (chatroomRepository.existsByMemberAndId(member, requestDto.getChatroomId())) {
			List<Chat> chats = chatRepository.findChatsByChatroomId(requestDto.getChatroomId());

			return chats.stream()
					.map(c -> chatroomModelMapper.map(c, ChatResponseDto.class))
					.collect(toList());
		}
		throw new ChatroomException("채팅방 소속 회원만이 채팅을 불러올 수 있습니다!");
	}

	public ChatResponseDto getChat(ChatGetRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (chatroomRepository.existsByMemberAndId(member, requestDto.getChatroomId())) {
			Chat chat =
					chatRepository
							.findByChatroomIdAndId(requestDto.getChatroomId(), requestDto.getChatId())
							.orElseThrow(() -> new ChatroomException("존재하지 않는 채팅입니다!"));

			return chatroomModelMapper.map(chat, ChatResponseDto.class);
		}
		throw new ChatroomException("채팅방 소속 회원만이 채팅을 불러올 수 없습니다!");
	}

	public Boolean isWrongPassword(Chatroom chatroom, String given_password) {
		ChatroomSetting setting = chatroom.getChatroomSetting();
		String password = setting.getPassword();

		return !password.equals(given_password);
	}

	public void disconnectSession(Long chatroom_id, String session_id) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		accessor.setSessionId(session_id);
		accessor.setDestination("/sub/chatroom/" + chatroom_id);
		messagingTemplate.convertAndSend(
				"/sub/chatroom/" + chatroom_id, "Disconnect", accessor.getMessageHeaders());
	}

	public void increase(Long chatroomId, String sessionId) {
		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);
		ChatroomSetting setting = chatroom.getChatroomSetting();
		if (setting.getCount() >= setting.getMaxCount()) {
			disconnectSession(chatroom.getId(), sessionId);
			return;
		}
		setting.setCount(setting.getCount() + 1);
		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);
	}

	public void decrease(Long chatroomId, String sessionId) {
		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);
		ChatroomSetting setting = chatroom.getChatroomSetting();
		if (setting.getCount() < 1) disconnectSession(chatroomId, sessionId);
		setting.setCount(setting.getCount() - 1);
		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);
	}
}
