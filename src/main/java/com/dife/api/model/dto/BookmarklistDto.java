package com.dife.api.model.dto;

import com.dife.api.model.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarklistDto {
	private Long id;
	private String message;
	private Long sender_id;

	public BookmarklistDto(Bookmark bookmark) {
		this.id = bookmark.getId();
		this.message = bookmark.getMessage();
		this.sender_id = bookmark.getChatroom().getMember().getId();
	}
}
