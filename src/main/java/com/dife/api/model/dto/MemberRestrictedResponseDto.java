package com.dife.api.model.dto;

import com.dife.api.model.File;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "표시용 회원정보 응답 데이터 전송 객체")
public class MemberRestrictedResponseDto {

	private Long id;

	@Schema(description = "이메일 주소", example = "gusuyeon@gmail.com")
	private String email;

	@Schema(description = "닉네임", example = "sooya")
	private String username;

	@Schema(description = "국가코드", example = "KO")
	private String country;

	@Schema(description = "프로필 공개 여부", example = "true")
	private Boolean isPublic;

	private String settingLanguage = "EN";

	@Schema(description = "S3에 저장되는 프로필 이미지", example = "cookie.jpeg")
	private File profileImg;
}
