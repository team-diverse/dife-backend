package com.diverse.dife.entity.matching;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
// 매칭 기능
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="matching_id")
    private Long id;

    // 매칭 종류 선택 -> 일대일 or 그룹
    @Enumerated(EnumType.STRING)
    private Category category;

    // 매칭 시간
    private LocalDateTime date;

}
