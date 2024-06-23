package com.dife.api.model.dto;

import com.dife.api.model.Post;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponseDto {

	private Long id;
	private String message;
	private Post post;
}
