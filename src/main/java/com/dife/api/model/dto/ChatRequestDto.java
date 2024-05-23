package com.dife.api.model.dto;

import com.dife.api.model.ChatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {

	private ChatType chatType;
	private String password;
	private String message;
	private Long chatroom_id;
	private String sender;
}
