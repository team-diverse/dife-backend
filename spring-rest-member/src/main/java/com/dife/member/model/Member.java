package com.dife.member.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private Integer nation;


    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String student_id;

    @Column(nullable = false)
    private String major;

    private String introduction;

    private String profile_img;

    @Enumerated(EnumType.STRING)
    private Category mbti;

    @Column(nullable = false)
    private Boolean blind;

    private String nickname;
}
