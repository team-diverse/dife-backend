package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.*;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatroomService {

	private final ChatroomRepository chatroomRepository;
	private final HobbyRepository hobbyRepository;
	private final LanguageRepository languageRepository;
	private final LikeChatroomRepository likeChatroomRepository;
	private final MemberRepository memberRepository;
	private final ChatRepository chatRepository;

	private final BlockService blockService;

	@Autowired
	@Qualifier("memberModelMapper")
	private ModelMapper memberModelMapper;

	private final ModelMapper modelMapper;

	@Autowired
	@Qualifier("chatroomModelMapper")
	private ModelMapper chatroomModelMapper;

	private final FileService fileService;

	public Boolean isDuplicate(String name) {
		return chatroomRepository.existsByName(name);
	}

	public List<ChatroomResponseDto> getChatrooms(ChatroomType type, String memberEmail) {

		List<Chatroom> chatrooms;

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (type == ChatroomType.SINGLE)
			chatrooms = chatroomRepository.findAllByChatroomTypeAndMember(ChatroomType.SINGLE, member);
		else if (type == ChatroomType.GROUP)
			chatrooms = chatroomRepository.findAllByChatroomType(ChatroomType.GROUP);
		else chatrooms = chatroomRepository.findAllByChatroomTypeAndManager(ChatroomType.GROUP, member);

		return getChatroomResponseDtos(chatrooms, member);
	}

	public ChatroomResponseDto createChatroom(
			MultipartFile profileImg,
			ChatroomType chatroomType,
			String name,
			String description,
			Long toMemberId,
			Optional<Integer> maxCount,
			Set<GroupPurposeType> purposes,
			Set<String> hobbies,
			Set<String> languages,
			Boolean isPublic,
			String password,
			String memberEmail)
			throws IOException {
		switch (chatroomType) {
			case GROUP:
				return createGroupChatroom(
						profileImg,
						name,
						description,
						memberEmail,
						maxCount,
						purposes,
						hobbies,
						languages,
						isPublic,
						password);
			case SINGLE:
				return createSingleChatroom(toMemberId, name, memberEmail);
		}
		throw new ChatroomException("유효한 채팅방 생성 접근이 아닙니다!");
	}

	public ChatroomResponseDto createGroupChatroom(
			MultipartFile profileImg,
			String name,
			String description,
			String memberEmail,
			Optional<Integer> maxCount,
			Set<GroupPurposeType> purposes,
			Set<String> hobbies,
			Set<String> languages,
			Boolean isPublic,
			String password)
			throws IOException {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		String trimmedName = name.trim();
		if (chatroomRepository.existsByName(trimmedName)) throw new ChatroomDuplicateException();
		if (trimmedName.isEmpty()) throw new ChatroomException("채팅방 이름은 필수사항입니다.");

		Chatroom chatroom = new Chatroom();

		chatroom.setManager(member);
		chatroom.getMembers().add(member);

		chatroom.setName(trimmedName);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();
		String trimmedDescription = description.trim();
		if (trimmedDescription.isEmpty())
			throw new ChatroomException("유효하지 않은 한줄소개입니다. 공백만 존재하는 한줄 소개는 허용되지 않습니다.");

		setting.setCount(setting.getCount() + 1);
		setting.setDescription(trimmedDescription);
		setting.setIsPublic(isPublic);

		chatroom.setChatroomSetting(setting);
		chatroomRepository.save(chatroom);

		return createDetail(
				chatroom, profileImg, setting, maxCount, purposes, hobbies, languages, isPublic, password);
	}

	public ChatroomResponseDto createDetail(
			Chatroom givenChatroom,
			MultipartFile profileImg,
			ChatroomSetting givenSetting,
			Optional<Integer> maxCount,
			Set<GroupPurposeType> purposes,
			Set<String> hobbies,
			Set<String> languages,
			Boolean isPublic,
			String password)
			throws IOException {

		if (purposes != null) {
			Set<GroupPurpose> groupPurposes =
					purposes.stream()
							.map(
									purposeType -> {
										GroupPurpose groupPurpose = new GroupPurpose();
										groupPurpose.setType(purposeType);
										groupPurpose.setChatroomSetting(givenSetting);
										return groupPurpose;
									})
							.collect(Collectors.toSet());
			givenSetting.setPurposes(groupPurposes);
		}

		if (hobbies != null) {

			Set<Hobby> existingHobbies = hobbyRepository.findHobbiesByChatroomSetting(givenSetting);
			Map<String, Hobby> nameToHobbyMap =
					existingHobbies.stream()
							.collect(
									Collectors.toMap(
											Hobby::getName, Function.identity(), (existing, replacement) -> existing));

			Set<Hobby> updatedHobbies = new HashSet<>();

			for (String hobbyName : hobbies) {
				if (nameToHobbyMap.containsKey(hobbyName)) {
					updatedHobbies.add(nameToHobbyMap.get(hobbyName));
				} else {
					Hobby nHobby = new Hobby();
					nHobby.setName(hobbyName);
					nHobby.setChatroomSetting(givenSetting);
					hobbyRepository.save(nHobby);
					updatedHobbies.add(nHobby);
				}
			}
			existingHobbies.stream()
					.filter(hobby -> !hobbies.contains(hobby.getName()))
					.forEach(hobbyRepository::delete);

			givenSetting.setHobbies(updatedHobbies);
		}

		if (languages != null) {

			Set<Language> existingLanguages =
					languageRepository.findLanguagesByChatroomSetting(givenSetting);
			Map<String, Language> nameToLanguageMap =
					existingLanguages.stream()
							.collect(
									Collectors.toMap(
											Language::getName, Function.identity(), (existing, replacement) -> existing));

			Set<Language> updatedLanguages = new HashSet<>();

			for (String languageName : languages) {
				if (nameToLanguageMap.containsKey(languageName)) {
					updatedLanguages.add(nameToLanguageMap.get(languageName));
				} else {
					Language nLanguage = new Language();
					nLanguage.setName(languageName);
					nLanguage.setChatroomSetting(givenSetting);
					languageRepository.save(nLanguage);
					updatedLanguages.add(nLanguage);
				}
			}
			existingLanguages.stream()
					.filter(language -> !languages.contains(language.getName()))
					.forEach(languageRepository::delete);

			givenSetting.setLanguages(updatedLanguages);
		}

		if (maxCount.isEmpty() && givenSetting.getMaxCount() == 2) {
			throw new ChatroomException("그룹 채팅방 인원을 입력해주세요!");
		} else if (!maxCount.isEmpty()) {
			Integer maxCountValue = maxCount.get();
			givenSetting.setMaxCount(maxCountValue);
		}

		if (isPublic != null) {
			givenSetting.setIsPublic(isPublic);
			if (!isPublic) givenSetting.setPassword(password);
		}

		if (profileImg != null && !profileImg.isEmpty()) {
			FileDto profileImgPath = fileService.upload(profileImg);
			File file = modelMapper.map(profileImgPath, File.class);
			givenSetting.setProfileImg(file);
		}

		givenChatroom.setChatroomSetting(givenSetting);
		chatroomRepository.save(givenChatroom);

		ChatroomResponseDto responseDto =
				chatroomModelMapper.map(givenSetting, ChatroomResponseDto.class);
		responseDto.setName(givenChatroom.getName());
		responseDto.setManager(
				memberModelMapper.map(givenChatroom.getManager(), MemberResponseDto.class));

		return responseDto;
	}

	public void checkManager(Long chatroomId, String memberEmail) {

		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		if (!chatroom.getManager().equals(member)) throw new MemberException("방장만이 채팅방 수정권한을 가집니다!");
	}

	public ChatroomResponseDto update(
			Long id,
			MultipartFile profileImg,
			Optional<Integer> maxCount,
			Set<GroupPurposeType> purposes,
			Set<String> hobbies,
			Set<String> languages,
			Boolean isPublic,
			String password,
			String memberEmail)
			throws IOException {
		Chatroom chatroom = chatroomRepository.getReferenceById(id);
		ChatroomSetting setting = chatroom.getChatroomSetting();

		checkManager(id, memberEmail);

		return createDetail(
				chatroom, profileImg, setting, maxCount, purposes, hobbies, languages, isPublic, password);
	}

	public ChatroomResponseDto createSingleChatroom(
			Long toMemberId, String name, String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member otherMember =
				memberRepository.findById(toMemberId).orElseThrow(MemberNotFoundException::new);

		Set<Member> blockedMembers = blockService.getBlackSet(currentMember);

		if (blockedMembers.contains(otherMember))
			throw new MemberException("차단된 사용자에게는 일대일 채팅을 보낼 수 없습니다!");

		if (chatroomRepository.existsSingleChatroomByMembers(
				currentMember, otherMember, ChatroomType.SINGLE))
			throw new SingleChatroomCreateDuplicateException();

		Chatroom chatroom = new Chatroom();
		chatroom.setName(name.trim());
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

	public ChatroomResponseDto getChatroom(Long id, String memberEmail) throws IOException {

		Chatroom chatroom = chatroomRepository.findById(id).orElseThrow(ChatroomNotFoundException::new);

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		ChatroomSetting setting = chatroom.getChatroomSetting();
		ChatroomResponseDto responseDto = chatroomModelMapper.map(setting, ChatroomResponseDto.class);
		if (chatroom.getChatroomType() == ChatroomType.GROUP) {
			responseDto.setManager(memberModelMapper.map(chatroom.getManager(), MemberResponseDto.class));
		}
		responseDto.setName(chatroom.getName());
		responseDto.setProfileImg(chatroom.getChatroomSetting().getProfileImg());
		if (chatroom.getMembers().contains(member)) responseDto.setIsEntered(true);
		responseDto.getMembers().add(memberModelMapper.map(member, MemberResponseDto.class));
		responseDto.setCreated(setting.getCreated());
		responseDto.setModified(setting.getModified());

		boolean isLiked = likeChatroomRepository.existsByChatroomAndMember(chatroom, member);
		responseDto.setIsLiked(isLiked);
		return responseDto;
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

	public List<ChatroomResponseDto> getFilterChatrooms(
			Set<String> hobbies,
			Set<GroupPurposeType> purposes,
			Set<String> languages,
			Integer maxCount,
			String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Set<String> safeHobbies = hobbies != null ? hobbies : Collections.emptySet();
		Set<GroupPurposeType> safePurposes = purposes != null ? purposes : Collections.emptySet();
		Set<String> safeLanguages = languages != null ? languages : Collections.emptySet();

		List<Chatroom> validChatrooms =
				chatroomRepository.findAll().stream()
						.filter(
								chatroom -> {
									ChatroomSetting setting = chatroom.getChatroomSetting();
									return safeHobbies.isEmpty()
											|| setting.getHobbies().stream()
													.anyMatch(hobby -> safeHobbies.contains(hobby.getName()));
								})
						.filter(
								chatroom -> {
									ChatroomSetting setting = chatroom.getChatroomSetting();
									return safePurposes.isEmpty()
											|| setting.getPurposes().stream()
													.anyMatch(purpose -> safePurposes.contains(purpose.getType()));
								})
						.filter(
								chatroom -> {
									ChatroomSetting setting = chatroom.getChatroomSetting();
									return safeLanguages.isEmpty()
											|| setting.getLanguages().stream()
													.anyMatch(language -> safeLanguages.contains(language.getName()));
								})
						.filter(
								chatroom -> {
									ChatroomSetting setting = chatroom.getChatroomSetting();
									return setting.getMaxCount() <= maxCount;
								})
						.collect(Collectors.toList());

		return getChatroomResponseDtos(validChatrooms, member);
	}

	public List<ChatroomResponseDto> getSearchChatrooms(String keyword, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		String trimmedKeyword = keyword.trim();
		List<Chatroom> chatrooms;

		chatrooms = chatroomRepository.findAllByKeywordSearch(trimmedKeyword);
		if (chatrooms.isEmpty()) throw new ChatroomNotFoundException();

		return getChatroomResponseDtos(chatrooms, member);
	}

	public List<ChatroomResponseDto> getLikedChatrooms(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		List<ChatroomLike> chatroomLikes = likeChatroomRepository.findChatroomLikeByMember(member);

		List<Chatroom> chatrooms =
				chatroomLikes.stream()
						.map(ChatroomLike::getChatroom)
						.distinct()
						.collect(Collectors.toList());

		return getChatroomResponseDtos(chatrooms, member);
	}

	public List<ChatroomResponseDto> getChatroomResponseDtos(
			List<Chatroom> chatrooms, Member member) {
		return chatrooms.stream()
				.map(
						chatroom -> {
							try {
								return getChatroom(chatroom.getId(), member.getEmail());
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						})
				.collect(Collectors.toList());
	}

	public List<ChatroomResponseDto> getRandomChatrooms(int count, String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

		List<Chatroom> randomChatrooms =
				chatroomRepository.findAll().stream()
						.filter(c -> c.getChatroomType() == ChatroomType.GROUP)
						.filter(c -> !c.getManager().getId().equals(member.getId()))
						.filter(c -> c.getChatroomSetting().getMaxCount() > c.getMembers().size())
						.collect(toList());

		log.info(String.valueOf(randomChatrooms.size()));
		if (randomChatrooms.isEmpty()) {
			return new ArrayList<>();
		}

		Collections.shuffle(randomChatrooms);
		List<Chatroom> chatrooms = randomChatrooms.stream().limit(count).toList();

		return getChatroomResponseDtos(chatrooms, member);
	}

	public void kickout(Long roomId, Long memberId, String memberEmail) {
		Chatroom chatroom =
				chatroomRepository.findById(roomId).orElseThrow(ChatroomNotFoundException::new);
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

		Member manager =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (!chatroom.getMembers().contains(member)) throw new MemberException("채팅방에 존재하는 회원이 아닙니다!");
		if (!manager.equals(chatroom.getManager())) throw new MemberException("방장만이 강퇴할 수 있습니다!");

		member.getChatrooms().remove(chatroom);
		chatroom.getMembers().remove(member);
	}
}
