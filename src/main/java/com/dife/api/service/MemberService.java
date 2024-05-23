package com.dife.api.service;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.config.EmailValidator;
import com.dife.api.exception.*;
import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.HobbyRepository;
import com.dife.api.repository.LanguageRepository;
import com.dife.api.repository.MemberRepository;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
	private final EmailValidator emailValidator;
	private final JavaMailSender javaMailSender;
	private final FileService fileService;
	private final ModelMapper modelMapper;

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private static final long ACCESS_TOKEN_VALIDITY_DURATION = 1000L;
	private static final long REFRESH_TOKEN_VALIDITY_DURATION = 90 * 24 * 60 * 1000L;

	@Value("${DIFE_PASSWORD}")
	private String difePassword;

	public Member registerEmailAndPassword(RegisterEmailAndPasswordRequestDto dto) {
		if (!emailValidator.isValidEmail(dto.getEmail())) {
			throw new RegisterException("유효하지 않은 이메일입니다");
		}

		if (dto.getPassword() == null || !dto.getPassword().matches("(?=.*[0-9a-zA-Z\\\\W]).{8,20}")) {
			throw new RegisterException("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상 포함된 8자 ~ 20자의 비밀번호여야 합니다.");
		}

		if (memberRepository.existsByEmail(dto.getEmail())) {
			throw new DuplicateMemberException("이미 가입되어있는 이메일입니다");
		}

		Member member = new Member();
		member.setEmail(dto.getEmail());
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		member.setPassword(encodedPassword);

		memberRepository.save(member);
		return member;
	}

	public Boolean checkUsername(String username) {
		if (memberRepository.existsByUsername(username)) {
			return false;
		}
		return true;
	}

	public Member registerDetail(
			String username,
			Boolean is_korean,
			String bio,
			MbtiCategory mbti,
			Set<String> hobbies,
			Set<String> languages,
			Boolean is_public,
			Long id,
			MultipartFile profile_img,
			MultipartFile verification_file) {
		Member member =
				memberRepository.findById(id).orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다!"));

		if (profile_img != null && !profile_img.isEmpty()) {
			FileDto profileImgPath = fileService.upload(profile_img);
			member.setProfile_file_id(profileImgPath.getName());
		} else {
			member.setProfile_file_id(null);
		}

		if (verification_file != null && !verification_file.isEmpty()) {
			FileDto verificationImgPath = fileService.upload(verification_file);
			member.setVerification_file_id(verificationImgPath.getName());
		} else {
			throw new RegisterException("재학생 인증은 필수 사항입니다!");
		}
		member.setUsername(username);
		member.setIs_korean(is_korean);
		member.setBio(bio);
		member.setMbti(mbti);

		Set<Hobby> myhobbies = new HashSet<>();

		if (hobbies != null) {
			for (String hob : hobbies) {
				Optional<Hobby> hobbyOptional = hobbyRepository.findByMemberAndName(member, hob);
				if (!hobbyOptional.isPresent()) {
					Hobby newHobby = new Hobby();
					newHobby.setName(hob);
					newHobby.setMember(member);
					hobbyRepository.save(newHobby);
					myhobbies.add(newHobby);
				} else {
					myhobbies.add(hobbyOptional.get());
				}
			}
		}

		member.setHobbies(myhobbies);

		Set<Language> mylanguages = new HashSet<>();

		if (languages == null || languages.isEmpty()) {
			throw new RegisterException("언어 선택은 필수입니다.");
		} else {
			for (String lan : languages) {
				Optional<Language> languageOptional = languageRepository.findByMemberAndName(member, lan);
				if (!languageOptional.isPresent()) {
					Language newLanguage = new Language();
					newLanguage.setName(lan);
					newLanguage.setMember(member);
					languageRepository.save(newLanguage);
					mylanguages.add(newLanguage);
				} else {
					mylanguages.add(languageOptional.get());
				}
			}
		}

		member.setLanguages(mylanguages);
		member.setIs_public(is_public);
		memberRepository.save(member);

		return member;
	}

	public Boolean checkToken(String password, String token) {

		if (!Objects.equals(password, difePassword) || jwtUtil.isExpired(token)) return true;
		return false;
	}

	public ResponseEntity<LoginSuccessDto> login(LoginDto dto) {

		String email = dto.getEmail();
		String password = dto.getPassword();
		if (email == null || email.isEmpty()) {
			throw new MemberException("이메일은 로그인 필수사항입니다!");
		}
		if (password == null || password.isEmpty()) {
			throw new MemberException("비밀번호는 로그인 필수사항입니다!");
		}

		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(email, password, null);
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Long member_id = customUserDetails.getId();
		String member_role = "USER";

		Boolean is_verified = customUserDetails.getIsVerified();
		String verification_file_id = customUserDetails.getVerificationFileId();

		String accessToken =
				jwtUtil.createJwt(
						member_id, member_role, "accessToken", "dife", ACCESS_TOKEN_VALIDITY_DURATION);
		String refreshToken =
				jwtUtil.createJwt(
						member_id, member_role, "refreshToken", "dife", REFRESH_TOKEN_VALIDITY_DURATION);

		ResponseEntity<LoginSuccessDto> responseEntity =
				ResponseEntity.status(CREATED)
						.body(
								new LoginSuccessDto(
										member_id, accessToken, refreshToken, is_verified, verification_file_id));

		return responseEntity;
	}

	public Member getMember(String email) {

		Member member =
				memberRepository
						.findByEmail(email)
						.orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다!"));

		return member;
	}

	public boolean changePassword(VerifyEmailDto emailDto) {

		if (!memberRepository.existsByEmail(emailDto.getEmail())) {
			return false;
		}
		Optional<Member> optionalMember = memberRepository.findByEmail(emailDto.getEmail());
		Member member = optionalMember.get();

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
		return true;
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
			memberResponseDtos.add(modelMapper.map(member, MemberResponseDto.class));
		}
		return memberResponseDtos;
	}
}
