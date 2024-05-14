package com.dife.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginSuccessDto {

	private final String accessToken;
	private final Long member_id;
	private final Boolean is_verified;
	private final String verification_file_id;
}
