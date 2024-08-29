package com.dife.api.model.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

	private Long id;

	private String content;

	private Boolean isPublic;

	private Boolean isLiked;

	private Boolean isBookmarked;

	private PostResponseDto post;

	private CommentResponseDto parentComment;

	private Integer likesCount;

	private Integer commentsCount;

	private Integer bookmarkCount;

	private MemberResponseDto writer;

	private LocalDateTime created;

	private LocalDateTime modified;
}
