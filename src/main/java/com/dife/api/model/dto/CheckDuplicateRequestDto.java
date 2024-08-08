package com.dife.api.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CheckDuplicateRequestDto {
	private String email;
	private String username;
}
