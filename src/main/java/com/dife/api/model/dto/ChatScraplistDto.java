package com.dife.api.model.dto;

import com.dife.api.model.ChatScrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatScraplistDto {
	private Long id;
	private String message;
	private String sender;

	public ChatScraplistDto(ChatScrap scrap) {
		this.id = scrap.getId();
		this.message = scrap.getMessage();
		this.sender = scrap.getSender();
	}
}
