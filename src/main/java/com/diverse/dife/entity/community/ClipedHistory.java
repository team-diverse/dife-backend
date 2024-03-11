package com.diverse.dife.entity.community;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
// 회원이 본인이 저장한 글을 기록할 수 있는 ClipedHistory
public class ClipedHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="clip_id")
    private Long id;


}
