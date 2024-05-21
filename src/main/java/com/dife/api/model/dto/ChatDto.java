package com.dife.api.model.dto;

import com.dife.api.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

	private String message;
	private Long chatroom_id;
	private String sender;

	public ChatDto(Chat chat) {
		this.message = chat.getMessage();
		this.chatroom_id = chat.getChatroom().getId();
		this.sender = chat.getSender();
	}
}
