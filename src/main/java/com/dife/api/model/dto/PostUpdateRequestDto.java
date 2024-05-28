package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDto {

	private String title;

	private String content;

	private Boolean isPublic;

	private BoardCategory boardType;

	private Long memberId;
}
