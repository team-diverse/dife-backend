package com.dife.api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatsGetByChatroomRequestDto {

	@NotNull private Long chatroomId;
}
