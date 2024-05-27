package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDto {

	private String title;

	private String content;

	private Boolean isPublic;

	private BoardCategory boardType;
}
