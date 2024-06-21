package com.dife.api.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {

	private String content;

	private Boolean isPublic;

	private Long postId;

	private Long parentCommentId;
}
