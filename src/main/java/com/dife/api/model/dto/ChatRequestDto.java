package com.dife.api.model.dto;

import com.dife.api.model.ChatType;
import com.dife.api.model.ChatroomType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {

	private ChatroomType chatroomType;
	private ChatType chatType;
	private String password;
	private String message;
	private Long chatroomId;
	private String username;
}
