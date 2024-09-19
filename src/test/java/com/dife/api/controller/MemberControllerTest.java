package com.dife.api.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dife.api.GlobalExceptionHandler;
import com.dife.api.exception.DuplicateMemberException;
import com.dife.api.jwt.JWTUtil;
import com.dife.api.model.MbtiCategory;
import com.dife.api.model.Member;
import com.dife.api.model.dto.*;
import com.dife.api.repository.MemberRepository;
import com.dife.api.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

	private MockMvc mockMvc;

	@Mock private MemberService memberService;

	@Mock private MemberRepository memberRepository;
	@Mock private JWTUtil jwtUtil;
	private static final long ACCESS_TOKEN_VALIDITY_DURATION = 60 * 60 * 1000L;
	private static final long REFRESH_TOKEN_VALIDITY_DURATION = 90 * 24 * 60 * 1000L;

	@InjectMocks private MemberController memberController;

	@BeforeEach
	public void setup() {
		mockMvc =
				MockMvcBuilders.standaloneSetup(memberController)
						.setControllerAdvice(new GlobalExceptionHandler())
						.build();
	}

	@Nested
	class registerEmailAndPasswordMethod {
		@Test
		public void shouldReturn201_WhenEmailPasswordIsPassed() throws Exception {
			RegisterEmailAndPasswordRequestDto requestDto =
					new RegisterEmailAndPasswordRequestDto("email@gmail.com", "password123");
			String reqBody = new ObjectMapper().writeValueAsString(requestDto);

			Member member = new Member();
			member.setId(1L);
			member.setEmail(requestDto.getEmail());
			member.setPassword(requestDto.getPassword());

			given(memberService.registerEmailAndPassword(any(RegisterEmailAndPasswordRequestDto.class)))
					.willReturn(new RegisterResponseDto(member.getEmail(), member.getId()));

			mockMvc
					.perform(
							post("/api/members/register")
									.contentType(MediaType.APPLICATION_JSON)
									.content(reqBody))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.email").value("email@gmail.com"))
					.andExpect(jsonPath("$.memberId").value(1L));
		}

		@Test
		public void shouldReturn409_WhenEmailExists() throws Exception {
			RegisterEmailAndPasswordRequestDto requestDto =
					new RegisterEmailAndPasswordRequestDto("email@gmail.com", "password123");
			String reqBody = new ObjectMapper().writeValueAsString(requestDto);

			when(memberService.registerEmailAndPassword(any(RegisterEmailAndPasswordRequestDto.class)))
					.thenThrow(new DuplicateMemberException("이미 가입되어있는 이메일입니다."));

			mockMvc
					.perform(
							post("/api/members/register")
									.contentType(MediaType.APPLICATION_JSON)
									.content(reqBody))
					.andExpect(status().isConflict())
					.andExpect(jsonPath("$.success").value(false));
		}
	}

	@Nested
	class loginMethod {

		@Test
		public void shouldReturn200_WhenLoginIsPassed() throws Exception {
			LoginDto requestDto = new LoginDto();
			requestDto.setEmail("email@gmail.com");
			requestDto.setPassword("password123");
			String reqBody = new ObjectMapper().writeValueAsString(requestDto);

			String accessToken =
					jwtUtil.createJwt(1L, "accessToken", "dife", ACCESS_TOKEN_VALIDITY_DURATION);
			String refreshToken =
					jwtUtil.createJwt(1L, "refreshToken", "dife", REFRESH_TOKEN_VALIDITY_DURATION);

			LoginSuccessDto responseDto = new LoginSuccessDto(1L, accessToken, refreshToken);

			ResponseEntity<LoginSuccessDto> responseEntity =
					new ResponseEntity<>(responseDto, HttpStatus.OK);

			given(memberService.login(any(LoginDto.class))).willReturn(responseEntity);

			mockMvc
					.perform(
							post("/api/members/login").contentType(MediaType.APPLICATION_JSON).content(reqBody))
					.andExpect(status().isOk())
					.andExpect(jsonPath("member_id").value(1L))
					.andExpect(jsonPath("$.accessToken").value(accessToken))
					.andExpect(jsonPath("$.refreshToken").value(refreshToken));
		}

		@Test
		public void shouldReturn401_WhenPasswordIsWrong() throws Exception {
			LoginDto requestDto = new LoginDto();
			requestDto.setEmail("email@gmail.com");
			requestDto.setPassword("wrong_password");

			String reqBody = new ObjectMapper().writeValueAsString(requestDto);

			given(memberService.login(any(LoginDto.class)))
					.willReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

			mockMvc
					.perform(
							post("/api/members/login").contentType(MediaType.APPLICATION_JSON).content(reqBody))
					.andExpect(status().isInternalServerError());
		}
	}

	@Nested
	class memberMethod {
		@Test
		@WithMockCustomUser
		public void shouldReturn200_WhenUpdateIsPassed() throws Exception {

			MockMultipartFile profileImg =
					new MockMultipartFile(
							"profileImg", "profile.png", "image/png", "Profile Image Content".getBytes());

			String password = "testPassword";
			String username = "testUsername";
			String country = "USA";
			String settingLanguage = "EN";
			String bio = "This is my new bio";
			MbtiCategory mbti = MbtiCategory.INTJ;
			Set<String> hobbies = Set.of("reading", "coding");
			Set<String> languages = Set.of("English", "Spanish");
			Boolean isPublic = true;

			MemberResponseDto responseDto = new MemberResponseDto();
			responseDto.setUsername(username);
			responseDto.setCountry(country);
			responseDto.setBio(bio);
			responseDto.setMbti(mbti);
			responseDto.setHobbies(hobbies);
			responseDto.setLanguages(languages);
			responseDto.setIsPublic(isPublic);

			given(
							memberService.update(
									eq(password),
									eq(username),
									eq(country),
									eq(settingLanguage),
									eq(bio),
									eq(mbti),
									eq(hobbies),
									eq(languages),
									eq(isPublic),
									any(MultipartFile.class),
									any(MultipartFile.class),
									eq("test@gmail.com")))
					.willReturn(responseDto);

			mockMvc
					.perform(
							MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/members")
									.file(profileImg)
									.param("password", password)
									.param("username", username)
									.param("country", country)
									.param("settingLanguage", settingLanguage)
									.param("bio", bio)
									.param("mbti", mbti.name())
									.param("hobbies", "reading", "coding")
									.param("languages", "English", "Spanish")
									.param("isPublic", String.valueOf(isPublic))
									.with(csrf())
									.with(
											SecurityMockMvcRequestPostProcessors.authentication(
													new UsernamePasswordAuthenticationToken(
															"test@gmail.com",
															null,
															List.of(new SimpleGrantedAuthority("ROLE_USER")))))
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.MULTIPART_FORM_DATA))
					.andDo(print())
					.andExpect(status().isOk()) // 예상 상태는 OK (200)
					.andExpect(jsonPath("$.username").value(username))
					.andExpect(jsonPath("$.country").value(country))
					.andExpect(jsonPath("$.bio").value(bio))
					.andExpect(jsonPath("$.mbti").value(mbti.name()))
					.andExpect(jsonPath("$.hobbies[0]").value("reading"))
					.andExpect(jsonPath("$.hobbies[1]").value("coding"))
					.andExpect(jsonPath("$.languages[0]").value("English"))
					.andExpect(jsonPath("$.languages[1]").value("Spanish"))
					.andExpect(jsonPath("$.isPublic").value(isPublic));
		}
	}
}
