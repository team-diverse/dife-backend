package com.diverse.dife.entity.schoolInfo.schoolMeal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SchoolMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="schoolmeal_id")
    private Long id;

    // 학식 날짜
    @Column(nullable = false)
    private String mealdate;

    // 학식 메뉴
    @Column(nullable = false)
    private String menu;

    // 학식 가격
    @Column(nullable = false)
    private String price;

    // 학식 구분
    @Column(nullable = false)
    private String corner;

    // 학식 중/석식 구분
    // 0: 중식 , 1: 석식
    private Integer type;

}
