package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDto {

    private String title;

    private String content;

    private Boolean is_public;

    private BoardCategory boardType;

    @NotNull
    @JsonProperty("member_id")
    private Long memberId;


}
