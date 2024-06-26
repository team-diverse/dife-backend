package com.dife.api.model.dto;

import com.dife.api.model.Comment;
import com.dife.api.model.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
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

	private Integer likesCount;

	@JsonProperty("writer")
	private Member writer;

	private List<Comment> childrenComments;

	@Schema(description = "프로필 생성 일시")
	private LocalDateTime created;

	@Schema(description = "프로필 수정 일시")
	private LocalDateTime modified;
}
