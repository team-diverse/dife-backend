package com.dife.api.model.dto;

import com.dife.api.model.Chat;
import com.dife.api.model.ChatScrap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseDto {

	private String message;
	private Long chatroom_id;
	private Long chat_id;
	private String sender;

	public ChatResponseDto(Chat chat) {
		this.chatroom_id = chat.getChatroom().getId();
		this.chat_id = chat.getId();
		this.message = chat.getMessage();
		this.sender = chat.getSender();
	}

	public ChatResponseDto(ChatScrap chatScrap) {
		this.chatroom_id = chatScrap.getChatroom().getId();
		this.chat_id = chatScrap.getId();
		this.message = chatScrap.getMessage();
		this.sender = chatScrap.getSender();
	}
}
