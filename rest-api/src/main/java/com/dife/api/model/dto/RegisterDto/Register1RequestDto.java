package com.dife.api.model.dto.RegisterDto;

import com.dife.api.model.MbtiCategory;
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
public class Register1RequestDto {

    @NotNull()
    @Email
    private String email;

    @NotNull()
    private String password;

}
