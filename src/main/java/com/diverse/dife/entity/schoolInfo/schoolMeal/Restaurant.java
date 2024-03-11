package com.diverse.dife.entity.schoolInfo.schoolMeal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="restaurant_id")
    private Long id;

    // 학식 식당
    @Enumerated(EnumType.STRING)
    private Category category;

    // 식당 위치
    @Column(nullable = false)
    private String location;

    // 식당 운영시간
    @Column(nullable = false)
    private String workingTime;


}
