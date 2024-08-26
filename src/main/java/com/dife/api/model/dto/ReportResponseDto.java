package com.dife.api.model.dto;

import com.dife.api.model.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {

	@Enumerated(EnumType.STRING)
	private ReportType type;

	private Post post;
	private Comment comment;
	private Member receiver;
	private Chatroom chatroom;
	private String message;
}
