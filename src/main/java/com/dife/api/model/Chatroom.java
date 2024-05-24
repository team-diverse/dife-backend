package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Transactional
@Entity
@Table(name = "chatroom")
public class Chatroom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name = "";

	private ChatroomType chatroomType = ChatroomType.GROUP;

	@Transient private Map<String, String> activeSessions = new ConcurrentHashMap<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "chatroom_setting_id")
	private ChatroomSetting chatroomSetting;

	@ManyToMany
	@JoinTable(
			name = "chatroom_member",
			joinColumns = @JoinColumn(name = "chatroom_id"),
			inverseJoinColumns = @JoinColumn(name = "member_id"))
	@JsonManagedReference
	private Set<Member> members = new HashSet<>();

	@OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Chat> chats = new HashSet<>();
}
