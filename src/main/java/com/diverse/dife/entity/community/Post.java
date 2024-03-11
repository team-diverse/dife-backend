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
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    // 포스팅 제목
    @Column(length = 20)
    private String title;

    // 포스팅 내용
    @Column(nullable = false)
    private String content;

    // 포스팅 일시
    private LocalDateTime date;

    // 조회수
    private Integer views;

    // 좋아요 수
    private Integer likes;

    // 스크랩 여부
    private Boolean cliped;

    // 익명 여부
    private Boolean blind;

    // 게시판 선택 -> Enum 타입 선택지
    @Enumerated(EnumType.STRING)
    private Category category;



}
