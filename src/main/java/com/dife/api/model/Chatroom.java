package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

	@Transient private Map<String, Long> activeSessions = new ConcurrentHashMap<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "chatroom_setting_id")
	private ChatroomSetting chatroom_setting;

	@OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY)
	private Set<Chat> chats;

	@OneToMany(mappedBy = "chatroom")

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
}
