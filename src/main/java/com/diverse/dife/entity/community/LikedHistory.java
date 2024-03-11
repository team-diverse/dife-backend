package com.diverse.dife.entity.community;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
// 회원이 본인이 남긴 좋아요 글을 기록할 수 있는 LikedHistory
public class LikedHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_id")
    private Long id;


}
