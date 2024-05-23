package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "language")
public class Language {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private Member member;

	@ManyToOne
	@JoinColumn(name = "chatroom_setting_id")
	@JsonIgnore
	private ChatroomSetting chatroom_setting;
}
