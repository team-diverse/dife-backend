package com.dife.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatGetRequestDto {

	@NotNull
	@JsonProperty("chatroom_id")
	private Long chatroomId;

	@NotNull
	@JsonProperty("chat_id")
	private Long chatId;
}
