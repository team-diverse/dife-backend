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
import com.dife.api.model.dto.RegisterEmailAndPasswordRequestDto;
import com.dife.api.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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

	@Test
	public void registerEmailAndPassword_ShouldReturn201_WhenEmailPasswordIsPassed()
			throws Exception {
		RegisterEmailAndPasswordRequestDto requestDto =
				new RegisterEmailAndPasswordRequestDto("email@example.com", "password123");
		String reqBody = new ObjectMapper().writeValueAsString(requestDto);

		Member member = new Member();
		member.setId(1L);
		member.setEmail(requestDto.getEmail());
		member.setPassword(requestDto.getPassword());

		given(memberService.registerEmailAndPassword(any(RegisterEmailAndPasswordRequestDto.class)))
				.willReturn(member);

		mockMvc
				.perform(
						post("/api/members/register").contentType(MediaType.APPLICATION_JSON).content(reqBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.member_id").value(member.getId()))
				.andExpect(jsonPath("$.email").value("email@example.com"));
	}

	@Test
	public void registerEmailAndPassword_ShouldReturn409_WhenEmailExists() throws Exception {
		RegisterEmailAndPasswordRequestDto requestDto =
				new RegisterEmailAndPasswordRequestDto("email@example.com", "password123");
		String reqBody = new ObjectMapper().writeValueAsString(requestDto);

		when(memberService.registerEmailAndPassword(any(RegisterEmailAndPasswordRequestDto.class)))
				.thenThrow(new DuplicateMemberException("이미 가입되어있는 이메일입니다."));

		mockMvc
				.perform(
						post("/api/members/register").contentType(MediaType.APPLICATION_JSON).content(reqBody))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success").value(false));
	}
}
