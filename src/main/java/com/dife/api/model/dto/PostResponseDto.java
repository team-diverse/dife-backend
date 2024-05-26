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

	@NotNull private String title;

	@NotNull private String content;

	@NotNull private BoardCategory boardType;

	@NotNull private Boolean is_public;

	private Integer viewCount;

	private LocalDateTime created;

	private LocalDateTime modified;

	@NotNull
	@JsonProperty("member")
	private Member Member;
}
