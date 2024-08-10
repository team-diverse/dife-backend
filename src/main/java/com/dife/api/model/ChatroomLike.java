package com.dife.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chatroom_likes")
public class ChatroomLike extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatroom;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
}
