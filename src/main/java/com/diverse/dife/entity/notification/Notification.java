package com.diverse.dife.entity.notification;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notification_id")
    private Long id;

    // 알림 수
    private Integer number;

    // 알림 시간
    private LocalDateTime date;

    // 알림 종류 선택 -> Enum 타입 선택지
    @Enumerated(EnumType.STRING)
    private Category category;


}
