package com.dife.api.model.dto;

import com.dife.api.model.Bookmark;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkDto {

	private String message;
	private Long chatroom_id;
	private Long chat_id;
	private String sender;

	public BookmarkDto(Bookmark bookmark) {
		this.chatroom_id = bookmark.getChatroom().getId();
		this.chat_id = bookmark.getId();
		this.message = bookmark.getMessage();
		this.sender = bookmark.getSender();
	}
}
