package com.dife.api.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dife.api.GlobalExceptionHandler;
import com.dife.api.exception.ChatroomDuplicateException;
import com.dife.api.exception.ChatroomException;
import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.ChatroomType;
import com.dife.api.model.dto.GroupChatroomRequestDto;
import com.dife.api.service.ChatroomService;
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
public class ChatroomControllerTest {

	private MockMvc mockMvc;

	@Mock private ChatroomService chatroomService;

	@InjectMocks private ChatroomController chatroomController;

	@BeforeEach
	public void setUp() {
		mockMvc =
				MockMvcBuilders.standaloneSetup(chatroomController)
						.setControllerAdvice(new GlobalExceptionHandler())
						.build();
	}

	@Test
	public void createGroupChatroom_ShouldReturn_201_WhenNameAndDescriptionIsPassed()
			throws Exception {

		String name = "exampleChatroomName";
		String description = "exampleDescription";

		Chatroom chatroom = new Chatroom();
		chatroom.setId(1L);
		chatroom.setName(name);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();
		setting.setDescription(description);
		chatroom.setChatroom_setting(setting);

		GroupChatroomRequestDto requestDto = new GroupChatroomRequestDto(chatroom);
		String reqBody = new ObjectMapper().writeValueAsString(requestDto);

		given(chatroomService.createGroupChatroom(name, description)).willReturn(chatroom);
		mockMvc
				.perform(
						post("/api/chats/")
								.param("name", name)
								.param("description", description)
								.contentType(MediaType.MULTIPART_FORM_DATA)
								.content(reqBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.roomId").value(chatroom.getId()))
				.andExpect(jsonPath("$.name").value("exampleChatroomName"))
				.andExpect(jsonPath("$.description").value("exampleDescription"));
	}

	@Test
	public void createGroupChatroom_ShouldReturn_409_WhenNameExists() throws Exception {
		String name = "exampleChatroomName";
		String description = "exampleDescription";

		Chatroom chatroom = new Chatroom();
		chatroom.setId(1L);
		chatroom.setName(name);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();
		setting.setDescription(description);
		chatroom.setChatroom_setting(setting);

		GroupChatroomRequestDto requestDto = new GroupChatroomRequestDto(chatroom);
		String reqBody = new ObjectMapper().writeValueAsString(requestDto);

		when(chatroomService.createGroupChatroom(requestDto.getName(), requestDto.getDescription()))
				.thenThrow(new ChatroomDuplicateException());

		mockMvc
				.perform(
						post("/api/chats/")
								.param("name", name)
								.param("description", description)
								.contentType(MediaType.MULTIPART_FORM_DATA)
								.content(reqBody))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	public void createGroupChatroom_ShouldReturn_422_WhenNameIsNull() throws Exception {
		String name = "";
		String description = "exampleDescription";

		Chatroom chatroom = new Chatroom();
		chatroom.setId(1L);
		chatroom.setName(name);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();
		setting.setDescription(description);
		chatroom.setChatroom_setting(setting);

		GroupChatroomRequestDto requestDto = new GroupChatroomRequestDto(chatroom);
		String reqBody = new ObjectMapper().writeValueAsString(requestDto);

		when(chatroomService.createGroupChatroom(requestDto.getName(), requestDto.getDescription()))
				.thenThrow(new ChatroomException("채팅방 이름은 필수 사항입니다."));

		mockMvc
				.perform(
						post("/api/chats/")
								.param("name", name)
								.param("description", description)
								.contentType(MediaType.MULTIPART_FORM_DATA)
								.content(reqBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	public void createGroupChatroom_ShouldReturn_422_WhenDescriptionIsNull() throws Exception {
		String name = "exampleChatroomName";
		String description = "";

		Chatroom chatroom = new Chatroom();
		chatroom.setId(1L);
		chatroom.setName(name);
		chatroom.setChatroomType(ChatroomType.GROUP);

		ChatroomSetting setting = new ChatroomSetting();
		setting.setDescription(description);
		chatroom.setChatroom_setting(setting);

		GroupChatroomRequestDto requestDto = new GroupChatroomRequestDto(chatroom);
		String reqBody = new ObjectMapper().writeValueAsString(requestDto);

		when(chatroomService.createGroupChatroom(requestDto.getName(), requestDto.getDescription()))
				.thenThrow(new ChatroomException("채팅방 한줄 소개는 필수 사항입니다."));

		mockMvc
				.perform(
						post("/api/chats/")
								.param("name", name)
								.param("description", description)
								.contentType(MediaType.MULTIPART_FORM_DATA)
								.content(reqBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void registerDetail() {}

	@Test
	void getGroupChatroom() {}
}
