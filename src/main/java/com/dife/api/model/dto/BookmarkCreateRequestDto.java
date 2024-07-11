package com.dife.api.model.dto;

import com.dife.api.model.BookmarkType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkCreateRequestDto {

	private BookmarkType type;
	private Long chatroomId;
	private Long chatId;
	private Long postId;
	private Long commentId;
}
