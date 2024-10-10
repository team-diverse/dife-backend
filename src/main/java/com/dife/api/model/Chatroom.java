package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	@Enumerated(EnumType.STRING)
	private ChatroomType chatroomType;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "chatroom_setting_id")
	@JsonIgnore
	private ChatroomSetting chatroomSetting;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	@JsonIgnore
	private Member manager;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "chatroom_member",
			joinColumns = @JoinColumn(name = "chatroom_id"),
			inverseJoinColumns = @JoinColumn(name = "member_id"))
	@JsonIgnore
	private Set<Member> members = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "chatroom_exited_members",
			joinColumns = @JoinColumn(name = "chatroom_id"),
			inverseJoinColumns = @JoinColumn(name = "member_id"))
	private Set<Member> exitedMembers = new HashSet<>();

	@OneToMany(mappedBy = "chatroom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Chat> chats = new HashSet<>();

	@OneToMany(mappedBy = "chatroom")
	@JsonIgnore
	private List<Report> reports;
}
