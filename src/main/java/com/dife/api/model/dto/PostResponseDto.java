package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
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

	private Boolean isLiked = false;

	private Integer bookmarkCount;

	private LocalDateTime created;

	private LocalDateTime modified;

	private List<FileDto> files;

	@NotNull
	@JsonProperty("writer")
	private Member Member;
}
