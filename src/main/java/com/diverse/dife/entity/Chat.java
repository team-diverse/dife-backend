package com.diverse.dife.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue
    @Column(name="chat_id")
    private long id;

    // 채팅 내용
    @Column(nullable = false)
    private String content;

    // 채팅 시간
    private LocalDateTime date;
}
