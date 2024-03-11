package com.diverse.dife.entity.schoolInfo.building;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    // String 타입 건물명
    @Enumerated(EnumType.STRING)
    private Category category;

    // 건물 서브 명칭 (ex. N1)
    private String subName;

    // 건물 위치
    private String location;

    // 건물 소개
    private String information;

    // 전화번호
    private String phoneNumber;

}
