package com.dife.api.model.dto;

import com.dife.api.model.Comment;
import com.dife.api.model.Post;
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

	private Post post;

	private Comment parentComment;

	private Integer likesCount;

	private Integer commentsCount;

	private Integer bookmarkCount;

	private MemberResponseDto writer;

	private LocalDateTime created;

	private LocalDateTime modified;
}
