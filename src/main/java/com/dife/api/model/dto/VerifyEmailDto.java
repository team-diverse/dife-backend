package com.dife.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailDto {

	@Email(message = "이메일 형식에 맞지 않습니다!")
	@Schema(description = "사용자 이메일", example = "example@gmail.com")
	private String email;
}
