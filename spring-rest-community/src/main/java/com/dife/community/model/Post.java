package com.dife.community.model;

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
    private Long id;

    @Column(length = 20)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    private LocalDateTime date;

    private Boolean blind;

    @Enumerated(EnumType.STRING)
    private Category category;
}
