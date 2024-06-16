package com.dife.api.model.dto;

import com.dife.api.model.LikeType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikeCreateRequestDto {

	private LikeType likeType;

	private Long postId;

	private Long commentId;
}
