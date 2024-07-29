package com.dife.api.model.dto;

import com.dife.api.model.DeclarationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeclarationRequestDto {

	private DeclarationType type;
	private Long postId;
	private Long commentId;
	private String message;
}
