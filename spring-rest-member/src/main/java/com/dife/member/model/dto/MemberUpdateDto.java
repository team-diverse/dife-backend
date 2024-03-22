package com.dife.member.model.dto;

import com.dife.member.model.MBTI_category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    @NotNull
    private Boolean is_korean;

    @NotNull
    private String bio;

    private String file_id;

    private MBTI_category mbti;

    @NotNull
    private Boolean is_public;

    private String nickname;
}
