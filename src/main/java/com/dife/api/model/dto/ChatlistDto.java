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
public class ChatlistDto {
	private Long id;
	private String message;
	private Long sender_id;

	public ChatlistDto(Chat chat) {
		this.id = chat.getId();
		this.message = chat.getMessage();
		this.sender_id = chat.getChatroom().getMember().getId();
	}
}
