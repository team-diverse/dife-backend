package com.dife.api.model.dto;

import com.dife.api.model.Comment;
import com.dife.api.model.DeclarationType;
import com.dife.api.model.Member;
import com.dife.api.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeclarationResponseDto {

	private DeclarationType type;
	private Post post;
	private Comment comment;
	private Member receiver;
	private String message;
}
