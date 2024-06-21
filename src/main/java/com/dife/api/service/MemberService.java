package com.dife.api.service;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.config.RegisterValidator;
import com.dife.api.exception.*;
import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.HobbyRepository;
import com.dife.api.repository.LanguageRepository;
import com.dife.api.repository.MemberRepository;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private final LanguageRepository languageRepository;
	private final HobbyRepository hobbyRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final RegisterValidator registerValidator;
	private final JavaMailSender javaMailSender;
	private final FileService fileService;
	private final ModelMapper modelMapper;

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

		memberRepository.save(member);
		return modelMapper.map(member, RegisterResponseDto.class);
	}

	public Boolean checkUsername(String username) {
		if (memberRepository.existsByUsername(username)) {
			return false;
		}
		return true;
	}

	public MemberResponseDto registerDetail(
			String username,
			Boolean isKorean,
			String bio,
			MbtiCategory mbti,
			Set<String> hobbies,
			Set<String> languages,
			Boolean isPublic,
			Long id,
			MultipartFile profileImg,
			MultipartFile verificationFile) {
		Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);


		if ("empty".equals(member.getVerificationFileName())
				&& (verificationFile == null || verificationFile.isEmpty())) {
			throw new MemberNotAddVerificationException();
		} else {
			if (verificationFile != null && !verificationFile.isEmpty()) {
				FileDto verificationImgPath = fileService.upload(verificationFile);
				member.setVerificationFileName(verificationImgPath.getName());
			}
		}

		if ("empty".equals(member.getProfileFileName())
				&& (profileImg == null || profileImg.isEmpty())) {
			member.setProfileFileName("empty");
		} else {
			if (profileImg != null && !profileImg.isEmpty()) {
				FileDto profileImgPath = fileService.upload(profileImg);
				member.setProfileFileName(profileImgPath.getOriginalName());
			}
		}

		member.setUsername(username);
		member.setIsKorean(isKorean);
		member.setBio(bio);
		member.setMbti(mbti);

		Set<String> safeHobbies = hobbies != null ? hobbies : Collections.emptySet();
		Set<String> safeLanguages = languages != null ? languages : Collections.emptySet();

		Set<Hobby> existingHobbies = hobbyRepository.findHobbiesByMember(member);
		Map<String, Hobby> nameToHobbyMap =
				existingHobbies.stream().collect(Collectors.toMap(Hobby::getName, Function.identity()));

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

		Set<Language> existingLanguages = languageRepository.findLanguagesByMember(member);
		Map<String, Language> nameToLanguageMap =
				existingLanguages.stream()
						.collect(Collectors.toMap(Language::getName, Function.identity()));

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

		member.setIsPublic(isPublic);
		memberRepository.save(member);

		return memberModelMapper.map(member, MemberResponseDto.class);
	}

	public ResponseEntity<LoginSuccessDto> login(LoginDto dto) {

		String email = dto.getEmail();
		String password = dto.getPassword();

		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(email, password, null);
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Long memberId = customUserDetails.getId();

		String accessToken =
				jwtUtil.createJwt(memberId, "accessToken", "dife", ACCESS_TOKEN_VALIDITY_DURATION);
		String refreshToken =
				jwtUtil.createJwt(memberId, "refreshToken", "dife", REFRESH_TOKEN_VALIDITY_DURATION);

		ResponseEntity<LoginSuccessDto> responseEntity =
				ResponseEntity.status(CREATED)
						.body(new LoginSuccessDto(memberId, accessToken, refreshToken));

		return responseEntity;
	}

	public MemberResponseDto getMember(String email) {

		Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
		MemberResponseDto responseDto = memberModelMapper.map(member, MemberResponseDto.class);

		responseDto.setProfilePresignUrl(fileService.getPresignUrl(member.getProfileFileName()));
		return responseDto;
	}

	public void changePassword(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

		String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();

		Random random = new Random();
		for (int i = 0; i < 8; i++) {
			sb.append(charset.charAt(random.nextInt(charset.length())));
		}

		String newPassword = sb.toString();
		String encodedPassword = passwordEncoder.encode(newPassword);
		member.setPassword(encodedPassword);
		memberRepository.save(member);

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(member.getEmail());
		simpleMailMessage.setSubject("🤿 DIFE 비밀번호 변경 메일 🤿");
		simpleMailMessage.setText(
				"비밀번호를 잊으셨나요? 🥹\n"
						+ "걱정하지 마세요!. 새 비밀번호를 부여해드릴게요!\n"
						+ "새 비밀번호 : "
						+ newPassword
						+ "\n"
						+ "안전한 인터넷 환경에서 항상 비밀번호를 관리하세요.");
		javaMailSender.send(simpleMailMessage);
	}

	public List<MemberResponseDto> getRandomMembers(int count, String email) {
		List<Member> validMembers =
				memberRepository.findAll().stream()
						.filter(member -> !member.getEmail().equals(email))
						.collect(Collectors.toList());

		if (validMembers.isEmpty()) {
			return new ArrayList<>();
		}

		Collections.shuffle(validMembers);
		List<Member> randomMembers = validMembers.stream().limit(count).toList();

		List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
		for (Member member : randomMembers) {
			memberResponseDtos.add(memberModelMapper.map(member, MemberResponseDto.class));
		}
		return memberResponseDtos;
	}

	public List<MemberResponseDto> getFilterMembers(
			Set<MbtiCategory> mbtiCategories, Set<String> hobbies, Set<String> languages) {

		Set<MbtiCategory> safeMbtiCategories =
				mbtiCategories != null ? mbtiCategories : Collections.emptySet();
		Set<String> safeHobbies = hobbies != null ? hobbies : Collections.emptySet();
		Set<String> safeLanguages = languages != null ? languages : Collections.emptySet();

		List<Member> validMembers =
				memberRepository.findAll().stream()
						.filter(
								member ->
										(safeMbtiCategories.contains(member.getMbti())
														|| member.getHobbies().stream()
																.anyMatch(hobby -> safeHobbies.contains(hobby.getName()))
														|| member.getLanguages().stream()
																.anyMatch(language -> safeLanguages.contains(language.getName())))
												&& member.getIsPublic().equals(true))
						.collect(Collectors.toList());

		List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
		for (Member member : validMembers) {
			memberResponseDtos.add(memberModelMapper.map(member, MemberResponseDto.class));
		}
		return memberResponseDtos;
	}
}
