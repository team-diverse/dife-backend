package com.dife.api.model.dto;

import com.dife.api.model.MBTI_category;
import com.dife.api.model.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String email;

    private String password;

    private Boolean is_korean;
    private String username;

    private String student_id;

    private String major;

    private String bio;

    private String file_id;

    private MBTI_category mbti;

    private Boolean is_public;

    private String nickname;

    private LocalDateTime created_at;

    private LocalDateTime last_online;

    private String role;

    private String tokenId;

    public MemberDto(Member request) {
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.student_id = request.getStudent_id();
        this.username = request.getUsername();
        this.bio = request.getBio();
        this.mbti = request.getMbti();
        this.is_korean = request.getIs_korean();
        this.is_public = request.getIs_public();
        this.major = request.getMajor();
        this.role = request.getRole();
        this.tokenId = request.getTokenId();
    }
}
