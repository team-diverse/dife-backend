package com.dife.api.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chatroom")
public class Chatroom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private ChatroomType chatroomType;

	@Embedded private ChatroomSetting setting;

	@OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<WebSocketSessionEntity> sessions = new HashSet<>();
}
