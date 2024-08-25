package com.dife.api.model.dto;

import com.dife.api.model.Chatroom;
import com.dife.api.model.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRedisDto {
	private Long id;

	@NotNull
	@Size(max = 300)
	private String message;

	private List<String> imgCode;

	private Member member;
	private Chatroom chatroom;
	private LocalDateTime created;
}
