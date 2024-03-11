package com.diverse.dife.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    // 학교 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 내/외국인 여부 -> 0: 내국인, 1: 외국인
    @Column(nullable = false)
    private Integer nation;

    // 프로필 이미지 url 필요

    // 이름, 학번, 학과는 받아올 수 있는 정보이기를,,,
    // 이름
    @Column(nullable = false)
    private String username;

    // 학번
    @Column(nullable = false, unique = false)
    private String student_id;

    // 학과
    @Column(nullable = false)
    private String major;

    // 닉네임
    private String nickname;

}
