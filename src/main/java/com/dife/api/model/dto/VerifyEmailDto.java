package com.dife.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class VerifyEmailDto {

    @NotNull(message = "이메일을 입력해주세요!")
    @Email(message = "이메일 형식에 맞지 않습니다!")
    @Schema(description = "사용자 이메일", example = "example@gmail.com")
    private String email;
}
