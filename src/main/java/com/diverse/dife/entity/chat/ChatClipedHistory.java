package com.diverse.dife.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
// 작성한 채팅을 스크랩 할 수 있는 ChatClipedHistory
public class ChatClipedHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_clip_id")
    private Long id;

}
