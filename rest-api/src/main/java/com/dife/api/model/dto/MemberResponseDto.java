package com.dife.api.model.dto;

import com.dife.api.model.Hobby;
import com.dife.api.model.Language;
import com.dife.api.model.MbtiCategory;
import com.dife.api.model.Member;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    private String email;

    private String name;

    private String student_id;

    private String major;

    private String role;

    private String username;

    private Boolean is_korean;

    private Boolean is_public;

    private MbtiCategory mbti;

    private Set<String> languages;

    private Set<String> hobbies;

    private String profile_file_id;

    private String bio;

    private Boolean is_verified;

    private LocalDateTime created;

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
