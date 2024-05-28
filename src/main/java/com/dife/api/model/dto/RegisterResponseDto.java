package com.dife.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원가입 응답 객체")
public class RegisterResponseDto {

	@Schema(description = "회원가입된 유저의 이메일", example = "example@gmail.com")
	private String email;

	@Schema(description = "생성된 유저 고유 번호", example = "1")
	private Long memberId;
}
