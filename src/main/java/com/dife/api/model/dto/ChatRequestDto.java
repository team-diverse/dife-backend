package com.dife.api.model.dto;

import com.dife.api.model.ChatType;
import com.dife.api.model.ChatroomType;
import java.time.LocalDateTime;
import java.util.List;
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
	private Long memberId;
	private String username;
	private LocalDateTime created;
	private List<String> imgCode;
}
