package com.dife.api.model.dto;

import com.dife.api.model.ChatScrap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatScrapDto {

	private String message;
	private Long chatroom_id;
	private Long chat_id;
	private String sender;

	public ChatScrapDto(ChatScrap chatScrap) {
		this.chatroom_id = chatScrap.getChatroom().getId();
		this.chat_id = chatScrap.getId();
		this.message = chatScrap.getMessage();
		this.sender = chatScrap.getSender();
	}
}
