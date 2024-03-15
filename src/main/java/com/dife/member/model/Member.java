package com.dife.member.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer nation;


    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique=true)
    private String student_id;

    @Column(nullable = false)
    private String major;


    private String nickname;
}
