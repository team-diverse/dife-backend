package com.dife.api.model.dto;

import com.dife.api.model.Chatroom;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {

	private Long id;

	@NotNull
	@Size(max = 300)
	private String message;

	@NotNull
	@JsonProperty("chatroom")
	private Chatroom chatroom;
}
