package com.dife.api.model.dto;

import com.dife.api.model.ChatType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {

	private ChatType chatType;
	private String password;
	private String message;
	private Long chatroomId;
	private String username;
}
