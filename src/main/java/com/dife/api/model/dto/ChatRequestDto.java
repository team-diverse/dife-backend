package com.dife.api.model.dto;

import com.dife.api.model.ChatType;
import com.dife.api.model.ChatroomType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {

	@Enumerated(EnumType.STRING)
	private ChatroomType chatroomType;

	@Enumerated(EnumType.STRING)
	private ChatType chatType;

	private String password;
	private String message;
	private Long chatroomId;
	private String authorization;
	private String username;
	private LocalDateTime created;
	private List<String> imgCode;
}
