package com.dife.api.model.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponseDto {

	private Long id;
	private String message;
	@Nullable private Long chatroomId;
}
