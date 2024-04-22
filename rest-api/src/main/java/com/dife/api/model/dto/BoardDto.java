package com.dife.api.model.dto;

import com.dife.api.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String username;


    public BoardDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getMember().getUsername();
    }
}