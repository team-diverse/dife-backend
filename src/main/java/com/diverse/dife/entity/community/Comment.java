package com.diverse.dife.entity.community;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
// 댓글 기능
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    // 댓글 내용
    @Column(nullable=false)
    private String content;

    // 익명 여부
    @Column(nullable = false)
    private Boolean blind;

    // 댓글 단 시간
    private LocalDateTime date;
}
