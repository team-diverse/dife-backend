package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

	private Long id;

	private String title;

	private String content;

	private BoardCategory boardType;

	private Boolean isPublic;

	private Integer likesCount;

	private Integer bookmarkCount;

	private LocalDateTime created;

	private LocalDateTime modified;

	@NotNull
	@JsonProperty("writer")
	private Member Member;
}
