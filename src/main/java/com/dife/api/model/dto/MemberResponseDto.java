package com.dife.api.model.dto;

import com.dife.api.model.File;
import com.dife.api.model.MbtiCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Schema(description = "전공")
	private String major;

	@Schema(description = "닉네임", example = "sooya")
	private String username;

	@Schema(description = "국가코드", example = "KO")
	private String country;

	@Schema(description = "프로필 공개 여부", example = "true")
	private Boolean isPublic;

	@Schema(description = "탈퇴된 회원 여부", example = "true")
	private Boolean isDeleted = false;

	@Schema(description = "프로필 좋아요 여부", example = "true")
	private Boolean isLiked = false;

	@Schema(description = "MBTI", example = "ENTJ")
	@Enumerated(EnumType.STRING)
	private MbtiCategory mbti;

	@Schema(description = "언어", example = "Korean, English")
	private Set<String> languages;

	@Schema(description = "취미", example = "Soccer")
	private Set<String> hobbies;

	@Schema(description = "S3에 저장되는 프로필 이미지", example = "cookie.jpeg")
	private File profileImg;

	@Schema(description = "Presigned S3경로")
	private String profilePresignUrl;

	@Schema(description = "한줄 소개", example = "hello")
	private String bio;

	@Schema(description = "재학생 인증 여부", example = "false")
	private Boolean isVerified;

	@Schema(description = "프로필 생성 일시")
	private LocalDateTime created;

	@Schema(description = "프로필 수정 일시")
	private LocalDateTime modified;
}
