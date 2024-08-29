package com.dife.api.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDto {

	private Long id;
	private PostResponseDto post;
}
