package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.Post;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private BoardCategory boardType;

    @NotNull
    private String username;

    private LocalDateTime created;

    private LocalDateTime modified;

    public PostResponseDto(Post request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.is_public = request.getIs_public();
        this.boardType = request.getBoardType();
        this.username = request.getMember().getUsername();
        this.created = request.getCreated();
        this.modified = request.getModified();
    }

}
