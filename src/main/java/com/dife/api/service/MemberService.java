package com.dife.api.service;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.config.RegisterValidator;
import com.dife.api.exception.*;
import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final BookmarkRepository bookmarkRepository;
	private final LikePostRepository likePostRepository;
	private final LikeCommentRepository likeCommentRepository;
	private final ConnectRepository connectRepository;
	private final NotificationTokenRepository notificationTokenRepository;
	private final NotificationRepository notificationRepository;
	private final LikeChatroomRepository likeChatroomRepository;
	private final TranslationRepository translationRepository;
	private final CommentRepository commentRepository;
	private final LanguageRepository languageRepository;
	private final HobbyRepository hobbyRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final RegisterValidator registerValidator;
	private final JavaMailSender javaMailSender;
	private final FileService fileService;
	private final BlockService blockService;
	private final ModelMapper modelMapper;
	private final ConnectService connectSerivce;
	private final PostService postService;
	private final CommentService commentService;
	private final LikeService likeService;

	@Autowired
	@Qualifier("memberModelMapper")
	private ModelMapper memberModelMapper;

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private static final long ACCESS_TOKEN_VALIDITY_DURATION = 60 * 60 * 1000L;
	private static final long REFRESH_TOKEN_VALIDITY_DURATION = 90 * 24 * 60 * 1000L;

	public RegisterResponseDto registerEmailAndPassword(RegisterEmailAndPasswordRequestDto dto) {

		registerValidator.registerValidate(dto.getEmail(), dto.getPassword());

		if (memberRepository.existsByEmail(dto.getEmail()))
			throw new DuplicateMemberException("이미 가입되어있는 이메일입니다");

		Member member = new Member();
		member.setEmail(dto.getEmail());

		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		member.setPassword(encodedPassword);

		member.setTranslationCount(0);

		memberRepository.save(member);
		return modelMapper.map(member, RegisterResponseDto.class);
	}

	public Boolean isDuplicate(String email, String username) {

		if (username != null) return memberRepository.existsByUsername(username);
		return memberRepository.existsByEmail(email);
	}

	public MemberResponseDto update(
			String password,
			String username,
			String country,
			String settingLanguage,
			String bio,
			MbtiCategory mbti,
			Set<String> hobbies,
			Set<String> languages,
			Boolean isPublic,
			MultipartFile profileImg,
			MultipartFile verificationFile,
			String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (password != null) member.setPassword(passwordEncoder.encode(password));

		//		 TODO: UNCOMMENT THIS AFTER BETA
		//		boolean notAddVerificationFile =
		//				(verificationFile == null || verificationFile.isEmpty())
		//						&& member.getUsername().equals("Diver");
		//		if (notAddVerificationFile) {
		//			throw new MemberNotAddVerificationException();
		//		}
		//		boolean hasToUploadVerificationFile = verificationFile != null &&
		// !verificationFile.isEmpty();
		//		if (hasToUploadVerificationFile) updateFile(member, verificationFile, true);
		//		 TODO: DELETE THIS AFTER BETA
		member.setIsVerified(true);

		boolean hasToUploadProfile = profileImg != null && !profileImg.isEmpty();
		if (hasToUploadProfile) updateFile(member, profileImg, false);

		if (username != null) member.setUsername(username);
		if (country != null) member.setCountry(country);
		member.setSettingLanguage(settingLanguage != null ? settingLanguage : "EN");
		if (bio != null) member.setBio(bio);
		if (mbti != null) member.setMbti(mbti);
		if (isPublic != null) member.setIsPublic(isPublic);

		if (hobbies != null) updateHobbies(member, hobbies);
		if (languages != null) updateLanguages(member, languages);

		memberRepository.save(member);

		return memberModelMapper.map(member, MemberResponseDto.class);
	}

	private void updateFile(Member member, MultipartFile givenFile, Boolean isVerificationFile) {
		FileDto verificationImgPath = fileService.upload(givenFile);
		File file = modelMapper.map(verificationImgPath, File.class);

		if (isVerificationFile) {
			file.setIsSecret(true);
			member.setVerificationFile(file);
		} else member.setProfileImg(file);
	}

	private void updateHobbies(Member member, Set<String> hobbies) {
		Set<String> safeHobbies = hobbies;

		Set<Hobby> existingHobbies = hobbyRepository.findHobbiesByMember(member);
		Map<String, Hobby> nameToHobbyMap =
				existingHobbies.stream()
						.collect(
								Collectors.toMap(
										Hobby::getName, Function.identity(), (existing, replacement) -> existing));

		Set<Hobby> updatedHobbies = new HashSet<>();

		for (String hobbyName : safeHobbies) {
			if (nameToHobbyMap.containsKey(hobbyName)) {
				updatedHobbies.add(nameToHobbyMap.get(hobbyName));
			} else {
				Hobby nHobby = new Hobby();
				nHobby.setName(hobbyName);
				nHobby.setMember(member);
				hobbyRepository.save(nHobby);
				updatedHobbies.add(nHobby);
			}
		}
		existingHobbies.stream()
				.filter(hobby -> !hobbies.contains(hobby.getName()))
				.forEach(hobbyRepository::delete);

		member.setHobbies(updatedHobbies);
	}

	private void updateLanguages(Member member, Set<String> languages) {
		Set<String> safeLanguages = languages;

		Set<Language> existingLanguages = languageRepository.findLanguagesByMember(member);
		Map<String, Language> nameToLanguageMap =
				existingLanguages.stream()
						.collect(
								Collectors.toMap(
										Language::getName, Function.identity(), (existing, replacement) -> existing));

		Set<Language> updatedLanguages = new HashSet<>();

		for (String languageName : safeLanguages) {
			if (nameToLanguageMap.containsKey(languageName)) {
				updatedLanguages.add(nameToLanguageMap.get(languageName));
			} else {
				Language nLanguage = new Language();
				nLanguage.setName(languageName);
				nLanguage.setMember(member);
				languageRepository.save(nLanguage);
				updatedLanguages.add(nLanguage);
			}
		}
		existingLanguages.stream()
				.filter(language -> !languages.contains(language.getName()))
				.forEach(languageRepository::delete);

		member.setLanguages(updatedLanguages);
	}

	public ResponseEntity<LoginSuccessDto> login(LoginDto dto) {

		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(), null);
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Long memberId = customUserDetails.getId();
		Boolean isCanceled = customUserDetails.getIsDeleted();

		if (isCanceled) throw new MemberException("탈퇴한 회원입니다!");

		String accessToken =
				jwtUtil.createJwt(memberId, "accessToken", "dife", ACCESS_TOKEN_VALIDITY_DURATION);
		String refreshToken =
				jwtUtil.createJwt(memberId, "refreshToken", "dife", REFRESH_TOKEN_VALIDITY_DURATION);

		return ResponseEntity.status(OK).body(new LoginSuccessDto(memberId, accessToken, refreshToken));
	}

	public MemberResponseDto getMember(String email) {

		Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
		MemberResponseDto responseDto = memberModelMapper.map(member, MemberResponseDto.class);
		return responseDto;
	}

	public MemberResponseDto getMemberById(Long id, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Member findMember = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

		if (!findMember.getIsPublic()) throw new MemberException("프로필 비공개 회원은 id 엔드포인트 접근 불가입니다!");

		MemberResponseDto responseDto = memberModelMapper.map(findMember, MemberResponseDto.class);

		responseDto.setIsLiked(likeService.isLikeListMember(member, findMember));
		return responseDto;
	}

	public Member getMemberEntityById(Long id) {
		return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
	}

	public Member getMemberEntityByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
	}

	public void deleteMember(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		deleteMemberBookmarks(member);
		deleteMemberPostLikes(member);
		deleteMemberChatroomLikes(member);
		deleteMemberChatroom(member);
		deleteMemberLanguage(member);
		deleteMemberHobbies(member);
		deleteMemberCommentLikes(member);
		deleteMemberComments(member);
		deleteMemberPosts(member);
		deleteMemberConnects(member);
		deleteMemberLikeList(member);
		deleteMemberBlackList(member);
		deleteMemberNotificationTokens(member);
		member.setIsDeleted(true);

		memberRepository.save(member);
	}

	public void deleteMemberBookmarks(Member member) {
		List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(member);

		for (Bookmark bookmark : bookmarks) {
			List<Translation> translations = translationRepository.findAllByBookmarks(bookmarks);

			for (Translation translation : translations) {
				translation.getBookmarks().remove(bookmark);

				if (translation.getBookmarks().isEmpty()) {
					translationRepository.delete(translation);
				} else {
					translationRepository.save(translation);
				}
			}

			member.getBookmarks().remove(bookmark);

			if (bookmark.getPost() != null) {
				bookmark.getPost().getBookmarks().remove(bookmark);
			}

			bookmarkRepository.delete(bookmark);
		}
	}

	public void deleteMemberPostLikes(Member member) {
		List<PostLike> postLikes = likePostRepository.findPostLikesByMember(member);
		for (PostLike postLike : postLikes) {
			if (postLike.getPost() != null) {
				postLike.getPost().getPostLikes().remove(postLike);
			}
			member.getPostLikes().remove(postLike);

			likePostRepository.delete(postLike);
		}
	}

	public void deleteMemberCommentLikes(Member member) {
		List<CommentLike> commentLikes = likeCommentRepository.findAllByMember(member);

		for (CommentLike commentLike : commentLikes) {
			if (commentLike.getComment() != null) {
				commentLike.getComment().getCommentLikes().remove(commentLike);
			}
			likeCommentRepository.delete(commentLike);
		}
	}

	public void deleteMemberChatroomLikes(Member member) {
		List<ChatroomLike> chatroomLikes = likeChatroomRepository.findChatroomLikeByMember(member);
		for (ChatroomLike chatroomLike : chatroomLikes) {
			if (chatroomLike.getChatroom() != null) {
				likeChatroomRepository.delete(chatroomLike);
			}
		}
	}

	public void deleteMemberChatroom(Member member) {
		Set<Chatroom> chatrooms = member.getChatrooms();
		for (Chatroom chatroom : chatrooms) {
			if (chatroom != null) {
				chatroom.setManager(null);
				chatroom.getMembers().remove(member);
			}
		}
	}

	public void deleteMemberLanguage(Member member) {
		Set<Language> languages = languageRepository.findLanguagesByMember(member);

		for (Language language : languages) {
			if (language != null) {
				member.getLanguages().remove(language);
				languageRepository.delete(language);
			}
		}
	}

	public void deleteMemberHobbies(Member member) {
		Set<Hobby> hobbies = hobbyRepository.findHobbiesByMember(member);

		for (Hobby hobby : hobbies) {
			if (hobby != null) {
				member.getHobbies().remove(hobby);
				hobbyRepository.delete(hobby);
			}
		}
	}

	public void deleteMemberComments(Member member) {
		List<Comment> comments = commentRepository.findAllByWriter(member);

		for (Comment comment : comments) {
			if (comment.getChildrenComments() == null || comment.getChildrenComments().isEmpty()) {
				comment.getPost().getComments().remove(comment);
				commentRepository.delete(comment);
			} else if (comment.getChildrenComments() != null || comment.getParentComment() != null) {
				comment.setWriter(null);
				comment.setContent(null);
				commentRepository.save(comment);
			}
		}
	}

	public void deleteMemberPosts(Member member) {
		List<Post> posts = postRepository.findAllByWriter(member);

		for (Post post : posts) {
			if (post != null) {
				post.setWriter(null);
				postRepository.save(post);
			}
		}
	}

	public void deleteMemberConnects(Member member) {
		List<Connect> sendConnects = connectRepository.findAllByFromMember(member);

		for (Connect connect : sendConnects) {
			if (connect != null) {
				member.getSent().remove(connect);
				connectRepository.delete(connect);
			}
		}

		List<Connect> toConnects = connectRepository.findAllByToMember(member);

		for (Connect connect : toConnects) {
			if (connect != null) {
				member.getReceived().remove(connect);
				connectRepository.delete(connect);
			}
		}
	}

	public void deleteMemberLikeList(Member member) {
		List<Member> likeList = member.getLikeList();

		Iterator<Member> iterator = likeList.iterator();
		while (iterator.hasNext()) {
			Member likedMember = iterator.next();
			likedMember.getLikeList().remove(member);
			iterator.remove();
		}
	}

	public void deleteMemberBlackList(Member member) {
		Set<MemberBlock> blackList = member.getBlackList();

		Iterator<MemberBlock> iterator = blackList.iterator();
		while (iterator.hasNext()) {
			MemberBlock memberBlock = iterator.next();
			Member blockedMember = memberBlock.getBlacklistedMember();
			blockedMember.getBlackList().removeIf(block -> block.getBlacklistedMember().equals(member));
			iterator.remove();
		}
	}

	public void deleteMemberNotificationTokens(Member member) {
		List<NotificationToken> notificationTokens = new ArrayList<>(member.getNotificationTokens());

		for (NotificationToken notificationToken : notificationTokens) {
			List<Notification> notifications = new ArrayList<>(notificationToken.getNotifications());
			for (Notification notification : notifications) {
				notificationToken.getNotifications().remove(notification);
				notificationRepository.delete(notification);
			}

			member.getNotificationTokens().remove(notificationToken);
			notificationTokenRepository.delete(notificationToken);
		}
	}

	public void changePassword(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

		String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();

		Random random = new Random();
		for (int i = 0; i < 8; i++) {
			sb.append(charset.charAt(random.nextInt(charset.length())));
		}

		String verifyCode = sb.toString();
		member.setVerifyCode(verifyCode);
		memberRepository.save(member);

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(member.getEmail());
		simpleMailMessage.setSubject("🤿 DIFE 비밀번호 변경 메일 🤿");
		simpleMailMessage.setText(
				"비밀번호를 잊으셨나요? 🥹\n"
						+ "걱정하지 마세요!. 비밀번호를 변경할 수 있는 인증번호를 부여해드릴게요!\n"
						+ "비밀번호 재설정 페이지로 돌아가 주세요!"
						+ "인증번호 : "
						+ verifyCode
						+ "\n"
						+ "안전한 인터넷 환경에서 항상 비밀번호를 관리하세요.");
		javaMailSender.send(simpleMailMessage);
	}

	public void verifyChangePasswordCode(String verifyCode, String newPassword, String memberEmail) {
		Member member = getMemberEntityByEmail(memberEmail);

		if (Objects.equals(member.getVerifyCode(), "")) throw new VerifyCodeNotFoundException();

		if (Objects.equals(verifyCode, member.getVerifyCode()) && newPassword != null) {
			String encodedPassword = passwordEncoder.encode(newPassword);
			member.setPassword(encodedPassword);
			memberRepository.save(member);

		} else if (!Objects.equals(verifyCode, member.getVerifyCode()))
			throw new MemberException("비밀번호 변경 코드가 일치하지 않습니다!");
	}

	public List<MemberResponseDto> getRandomMembers(int count, String email) {
		Member currentMember = getMemberEntityByEmail(email);

		List<Member> validRandomMembers =
				memberRepository.findAll().stream()
						.filter(member -> !member.getEmail().equals(email))
						.filter(this::isValidMember)
						.filter(member -> !connectSerivce.isConnected(currentMember, member))
						.filter(member -> !connectSerivce.hasPendingConnect(currentMember, member))
						.filter(member -> !blockService.isBlackListMember(currentMember, member))
						.collect(Collectors.toList());

		if (validRandomMembers.isEmpty()) return new ArrayList<>();

		Collections.shuffle(validRandomMembers);
		List<Member> randomMembers = validRandomMembers.stream().limit(count).toList();

		List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
		for (Member member : randomMembers)
			memberResponseDtos.add(getMemberResponseDto(member, currentMember));
		return memberResponseDtos;
	}

	public List<MemberResponseDto> getFilterMembers(
			Set<MbtiCategory> mbtiCategories,
			Set<String> hobbies,
			Set<String> languages,
			String memberEmail) {

		Member currentMember =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Set<MbtiCategory> safeMbtiCategories =
				mbtiCategories != null ? mbtiCategories : Collections.emptySet();
		Set<String> safeHobbies = hobbies != null ? hobbies : Collections.emptySet();
		Set<String> safeLanguages = languages != null ? languages : Collections.emptySet();

		Set<Member> blockedMembers = blockService.getBlackSet(currentMember);

		List<Member> validMembers =
				memberRepository.findAll().stream()
						.filter(this::isValidMember)
						.filter(member -> !blockedMembers.contains(member))
						.filter(
								member ->
										safeLanguages.isEmpty()
												|| member.getLanguages().stream()
														.anyMatch(language -> safeLanguages.contains(language.getName())))
						.filter(
								member ->
										safeMbtiCategories.isEmpty() || safeMbtiCategories.contains(member.getMbti()))
						.filter(
								member ->
										safeHobbies.isEmpty()
												|| member.getHobbies().stream()
														.anyMatch(hobby -> safeHobbies.contains(hobby.getName())))
						.collect(Collectors.toList());

		if (validMembers.isEmpty()) throw new MemberNotFoundException();

		List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
		for (Member member : validMembers)
			memberResponseDtos.add(getMemberResponseDto(member, currentMember));
		return memberResponseDtos;
	}

	public List<MemberResponseDto> getSearchMembers(String keyword, String memberEmail) {

		Member currentMember =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		String trimmedKeyword = keyword.trim();
		List<Member> members = memberRepository.findAllByKeywordSearch(trimmedKeyword);

		Set<Member> blockedMembers = blockService.getBlackSet(currentMember);

		if (members.isEmpty()) throw new MemberNotFoundException();

		return members.stream()
				.filter(this::isValidMember)
				.filter(member -> !blockedMembers.contains(member))
				.map(member -> getMemberResponseDto(member, currentMember))
				.collect(Collectors.toList());
	}

	private MemberResponseDto getMemberResponseDto(Member member, Member currentMember) {
		MemberResponseDto responseDto = memberModelMapper.map(member, MemberResponseDto.class);
		responseDto.setIsLiked(likeService.isLikeListMember(currentMember, member));
		return responseDto;
	}

	public boolean isValidMember(Member member) {
		return member.getIsPublic() && member.getIsVerified();
	}

	public List<PostResponseDto> getMemberPosts(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Sort sort = Sort.by(Sort.Direction.DESC, "created");
		List<Post> posts = postRepository.findPostsByWriter(member, sort);

		return posts.stream()
				.map(post -> postService.getPost(post.getId(), memberEmail))
				.collect(toList());
	}

	public List<CommentResponseDto> getComments(String memberEmail) {
		Member writer =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Sort sort = Sort.by(Sort.Direction.DESC, "created");
		List<Comment> comments = commentRepository.findCommentsByWriter(writer, sort);

		return comments.stream()
				.map(comment -> commentService.getComment(comment, writer))
				.collect(toList());
	}

	public List<MemberResponseDto> getLikeMembers(String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		return member.getLikeList().stream()
				.map(ll -> modelMapper.map(ll, MemberResponseDto.class))
				.collect(Collectors.toList());
	}
}
