package com.dife.api.model.dto;

import com.dife.api.model.MbtiCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 응답 데이터 전송 객체")
public class MemberResponseDto {

	private Long id;

	@Schema(description = "이메일 주소", example = "gusuyeon@gmail.com")
	private String email;

	@Schema(description = "실명")
	private String name;

	@Schema(description = "학번")
	private String studentId;

	@Schema(description = "전공")
	private String major;

	@Schema(description = "닉네임", example = "sooya")
	private String username;

	@Schema(description = "내국인 여부", example = "true")
	private Boolean isKorean;

	@Schema(description = "프로필 공개 여부", example = "true")
	private Boolean isPublic;

	@Schema(description = "MBTI", example = "ENTJ")
	private MbtiCategory mbti;

	@Schema(description = "언어", example = "Korean, English")
	private Set<String> languages;

	@Schema(description = "취미", example = "Soccer")
	private Set<String> hobbies;

	@Schema(description = "프로필 파일 이름", example = "cookie.jpeg")
	private String profileFileName;

	@Schema(description = "한줄 소개", example = "hello")
	private String bio;

	@Schema(description = "재학생 인증 여부", example = "false")
	private Boolean isVerified;

	@Schema(description = "프로필 생성 일시")
	private LocalDateTime created;

	@Schema(description = "프로필 수정 일시")
	private LocalDateTime modified;
}
