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
public class RegisterEmailAndPasswordRequestDto {

    @NotNull()
    @Email
    private String email;

    @NotNull()
    private String password;

}
