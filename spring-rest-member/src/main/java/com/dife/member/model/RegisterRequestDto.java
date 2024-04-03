package com.dife.member.model;

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

    @NotNull()
    private String bio;

    @NotNull()
    private String password;

    private String role;


    public RegisterRequestDto(RegisterRequestDto request) {
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.student_id = request.getStudent_id();
        this.username = request.getUsername();
        this.bio = request.getBio();
        this.is_korean = request.getIs_korean();
        this.is_public = request.getIs_public();
        this.major = request.getMajor();
        this.role = request.getRole();
    }
}
