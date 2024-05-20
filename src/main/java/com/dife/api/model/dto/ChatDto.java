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
	private Long chat_id;
<<<<<<< HEAD
	private String sender;

	public ChatDto(Chat chat) {
		this.message = chat.getMessage();
		this.chatroom_id = chat.getChatroom().getId();
		this.chat_id = chat.getId();
		this.sender = chat.getSender();
	}
=======
	private Long sender_id;
>>>>>>> 1309942 (feat/chat-security-4: 채팅 시큐리티 적용 준비)
}
