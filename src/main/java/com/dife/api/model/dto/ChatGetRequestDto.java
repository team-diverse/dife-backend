package com.dife.api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatGetRequestDto {

	@NotNull private Long chatroomId;

	@NotNull private Long chatId;
}
