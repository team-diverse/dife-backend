package com.dife.api.model.dto;

import com.dife.api.model.BookmarkType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkCreateRequestDto {

	@Enumerated(EnumType.STRING)
	private BookmarkType type;

	private Long chatroomId;
	private Long chatId;
	private Long postId;
}
