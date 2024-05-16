package com.dife.api.model;

import jakarta.persistence.*;
import java.util.Map;
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

	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, String> activeSessions = new ConcurrentHashMap<>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "chatroom_setting_id")
	private ChatroomSetting chatroom_setting;
}
