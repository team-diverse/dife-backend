package com.dife.member.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean is_korean;


    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String student_id;

    @Column(nullable = false)
    private String major;

    private String bio;

    private String file_id;

    @Enumerated(EnumType.STRING)
    private Category mbti;

    @Column(nullable = false)
    private Boolean is_public;

    private String nickname;

    private LocalDateTime created_at;

    private LocalDateTime last_online;
}
