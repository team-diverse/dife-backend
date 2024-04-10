package com.dife.api.model.dto;

import com.dife.api.model.MBTI_category;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    @NotNull
    private String bio;

    @NotNull
    private Boolean is_public;

    @NotNull
    private String major;

    private MBTI_category mbti;

    private String nickname;
}
