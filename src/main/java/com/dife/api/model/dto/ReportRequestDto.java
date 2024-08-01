package com.dife.api.model.dto;

import com.dife.api.model.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {

	private ReportType type;
	private Long postId;
	private Long commentId;
	private Long receiverId;
	private String message;
}
