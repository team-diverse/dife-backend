package com.dife.api.model.dto.RegisterDto;

import com.dife.api.model.MbtiCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Register7RequestDto {

    @NotNull()
    private String username;

    private Boolean is_korean;

    private String bio;

    @NotNull()
    private MbtiCategory mbti;

    @NotNull()
    private Set<String> hobbies;

    @NotNull()
    private Set<String> languages;

}
