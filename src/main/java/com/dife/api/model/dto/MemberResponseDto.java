package com.dife.api.model.dto;

import com.dife.api.model.Hobby;
import com.dife.api.model.Language;
import com.dife.api.model.MbtiCategory;
import com.dife.api.model.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 응답 데이터 전송 객체")
public class MemberResponseDto {

    @Schema(description = "이메일 주소", example = "gusuyeon@gmail.com")
    private String email;

    @Schema(description = "실명")
    private String name;

    @Schema(description = "학번")
    private String student_id;

    @Schema(description = "전공")
    private String major;

    @Schema(description = "역할")
    private String role;

    @Schema(description = "닉네임", example = "sooya")
    private String username;

    @Schema(description = "내국인 여부", example = "true")
    private Boolean is_korean;

    @Schema(description = "프로필 공개 여부", example = "true")
    private Boolean is_public;

    @Schema(description = "MBTI", example = "ENTJ")
    private MbtiCategory mbti;

    @Schema(description = "언어",  example = "Korean, English")
    private Set<String> languages;

    @Schema(description = "취미",  example = "Soccer")
    private Set<String> hobbies;

    @Schema(description = "프로필 파일 이름", example = "cookie.jpeg")
    private String profile_file_id;

    @Schema(description = "한줄 소개", example = "hello")
    private String bio;

    @Schema(description = "재학생 인증 여부", example = "false")
    private Boolean is_verified;

    @Schema(description = "프로필 생성 일시")
    private LocalDateTime created;

    @Schema(description = "프로필 수정 일시")
    private LocalDateTime modified;

    public MemberResponseDto(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.student_id = member.getStudent_id();
        this.major = member.getMajor();
        this.role = member.getRole();
        this.username = member.getUsername();
        this.is_korean = member.getIs_korean();
        this.is_public = member.getIs_public();
        this.mbti = member.getMbti();
        if (member.getLanguages() != null && !member.getLanguages().isEmpty()) {
            this.languages = member.getLanguages().stream()
                    .map(Language::getName)
                    .collect(Collectors.toSet());
        }
        if (member.getHobbies() != null && !member.getHobbies().isEmpty()) {
            this.hobbies = member.getHobbies().stream()
                    .map(Hobby::getName)
                    .collect(Collectors.toSet());
        }
        this.profile_file_id = member.getProfile_file_id();
        this.bio = member.getBio();
        this.is_verified = member.getIs_verified();
        this.created = member.getCreated();
        this.modified = member.getModified();
    }

}
