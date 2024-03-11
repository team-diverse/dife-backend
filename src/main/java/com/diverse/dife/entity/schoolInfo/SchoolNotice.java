package com.diverse.dife.entity.schoolInfo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
// 학교 공지
public class SchoolNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="schoolNotice_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private String content;
}
