package com.dife.api.model.dto;

import com.dife.api.model.BOARD_category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Boolean is_public;

    @NotNull
    private BOARD_category boardType;


}
