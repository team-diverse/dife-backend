package com.dife.api.model.dto;

import com.dife.api.model.Translation;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponseDto {

	private Long id;
	private String message;
	private List<Translation> translations;
	private PostResponseDto post;
	private LocalDateTime created;
}
