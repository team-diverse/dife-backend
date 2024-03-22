package com.dife.member.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private List<String> roles;
}
