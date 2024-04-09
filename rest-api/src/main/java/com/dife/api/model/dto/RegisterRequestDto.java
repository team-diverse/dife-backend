package com.dife.api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotNull()
    @Email
    private String email;

    @NotNull()
    private String student_id;

    @NotNull()
    private String username;

    @NotNull()
    private String major;

    @NotNull()
    private Boolean is_public;

    @NotNull()
    private Boolean is_korean;

    private String bio;

    private String nickname;

    @NotNull()
    private String password;

    @NotNull()
    private String role;


    public RegisterRequestDto(RegisterRequestDto request) {
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.student_id = request.getStudent_id();
        this.username = request.getUsername();
        this.bio = request.getBio();
        this.nickname = request.getNickname();
        this.is_korean = request.getIs_korean();
        this.is_public = request.getIs_public();
        this.major = request.getMajor();
        this.role = request.getRole();
    }
}
