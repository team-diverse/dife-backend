package com.dife.api.model.dto;

import com.dife.api.model.ChatType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatEnterDto {

	private ChatType chatType;
	private String password;
	private Long chatroom_id;
	private String sender;
}
