package com.dife.api.model.dto;

import com.dife.api.model.Bookmark;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkDto {

	private String message;
	private Long bookmark_id;
	private String sender;

	public BookmarkDto(Bookmark bookmark) {
		this.bookmark_id = bookmark.getId();
		this.message = bookmark.getMessage();
		this.sender = bookmark.getSender();
	}
}
