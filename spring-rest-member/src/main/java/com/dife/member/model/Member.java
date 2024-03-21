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
    private MBTI_category mbti;

    @Column(nullable = false)
    private Boolean is_public;

    private String nickname;

    private LocalDateTime created_at;

    private LocalDateTime last_online;

    public void editPassword(String password)
    {
        this.password = password;
    }

    public void editUsername(String username)
    {
        this.username = username;
    }

    public void editBio(String bio)
    {
        this.bio = bio;
    }

    public void editFile_id(String file_id)
    {
        this.file_id = file_id;
    }

    public void editMbti(String mbti)
    {
        this.mbti = MBTI_category.valueOf(mbti);
    }
    public void editIs_public(Boolean is_public)
    {
        this.is_public = is_public;
    }
}
