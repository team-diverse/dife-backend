package com.dife.api.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarksGetByChatroomRequestDto {

	@NotNull private Long chatroomId;
}
