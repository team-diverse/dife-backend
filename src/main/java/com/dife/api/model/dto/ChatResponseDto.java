package com.dife.api.model.dto;

import com.dife.api.model.Bookmark;
import com.dife.api.model.Chat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseDto {

	private String message;
	private Long chatroom_id;
	private Long chat_id;
	private Long sender_id;

	public ChatResponseDto(Chat chat) {
		this.chatroom_id = chat.getChatroom().getId();
		this.chat_id = chat.getId();
		this.message = chat.getMessage();
		this.sender_id = chat.getChatroom().getMember().getId();
	}

	public ChatResponseDto(Bookmark bookmark) {
		this.chatroom_id = bookmark.getChatroom().getId();
		this.chat_id = bookmark.getId();
		this.message = bookmark.getMessage();
		this.sender_id = bookmark.getChatroom().getMember().getId();
	}
}
