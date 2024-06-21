package com.dife.api.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkCreateRequestDto {

	private Long chatroomId;
	private Long chatId;
	private Long postId;
}
