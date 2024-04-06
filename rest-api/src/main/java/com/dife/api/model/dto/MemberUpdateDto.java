package com.dife.api.model.dto;

import com.dife.api.model.MBTI_category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    @NotNull
    private String password;

    @NotNull
    private Boolean is_korean;

    @NotNull
    private String bio;

    @NotNull
    private MBTI_category mbti;

    @NotNull
    private Boolean is_public;

    private String nickname;
}
