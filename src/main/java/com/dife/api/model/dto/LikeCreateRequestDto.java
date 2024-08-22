package com.dife.api.model.dto;

import com.dife.api.model.LikeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikeCreateRequestDto {

	@Enumerated(EnumType.STRING)
	private LikeType type;

	private Long id;
}
