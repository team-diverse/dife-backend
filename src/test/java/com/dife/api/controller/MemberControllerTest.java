package com.dife.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dife.api.GlobalExceptionHandler;
import com.dife.api.exception.DuplicateMemberException;
import com.dife.api.model.Member;
import com.dife.api.model.dto.LoginDto;
import com.dife.api.model.dto.LoginSuccessDto;
import com.dife.api.model.dto.RegisterEmailAndPasswordRequestDto;
import com.dife.api.model.dto.RegisterResponseDto;
import com.dife.api.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

	private MockMvc mockMvc;

	@Mock private MemberService memberService;

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

			LoginSuccessDto responseDto =
					new LoginSuccessDto(
							1L,
							"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
							"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

			ResponseEntity<LoginSuccessDto> responseEntity =
					new ResponseEntity<>(responseDto, HttpStatus.OK);

			given(memberService.login(any(LoginDto.class))).willReturn(responseEntity);

			mockMvc
					.perform(
							post("/api/members/login").contentType(MediaType.APPLICATION_JSON).content(reqBody))
					.andExpect(status().isOk())
					.andExpect(jsonPath("member_id").value(1L))
					.andExpect(
							jsonPath("$.accessToken")
									.value(
											"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"))
					.andExpect(
							jsonPath("$.refreshToken")
									.value(
											"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"));
		}

		@Test
		public void shouldReturn500_WhenPasswordIsWrong() throws Exception {
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
}
