package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {
	@NotNull private BoardCategory boardCategory;
}
