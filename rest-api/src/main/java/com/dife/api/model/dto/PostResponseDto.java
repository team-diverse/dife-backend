package com.dife.api.model.dto;

import com.dife.api.model.BOARD_category;
import com.dife.api.model.Post;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Boolean is_public;

    @NotNull
    private BOARD_category boardType;

    @NotNull
    private String memberNickname;

    public PostResponseDto(Post request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.is_public = request.getIs_public();
        this.boardType = request.getBoardType();
        this.memberNickname = request.getMember().getNickname();
    }

}
