package com.dife.api.model.dto;

import com.dife.api.model.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원가입 응답 객체")
public class RegisterResponseDto {

	@Schema(description = "회원가입 성공여부", example = "true")
	private Boolean success;

	@Schema(description = "응답 메시지", example = "회원가입 성공")
	private String message;

	@Schema(description = "회원가입된 유저의 이메일", example = "example@gmail.com")
	private String email;

	@Schema(description = "생성된 유저 고유 번호", example = "1")
	private Long member_id;

	public RegisterResponseDto(Member member) {
		this.success = true;
		this.message = "회원가입 성공";
		this.email = member.getEmail();
		this.member_id = member.getId();
	}
}
