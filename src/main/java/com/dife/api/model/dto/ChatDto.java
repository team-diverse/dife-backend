package com.dife.api.model.dto;

import com.dife.api.model.ChatType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

	private ChatType chatType;
	private String message;
	private Long chatroom_id;
	private String sender;
}
