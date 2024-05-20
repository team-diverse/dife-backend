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
	private String sender;

	public BookmarklistDto(Bookmark scrap) {
		this.id = scrap.getId();
		this.message = scrap.getMessage();
		this.sender = scrap.getSender();
	}
}
