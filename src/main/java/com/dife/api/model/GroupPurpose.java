package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "group_purpose")
public class GroupPurpose {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private GroupPurposeType type;

	@ManyToOne
	@JoinColumn(name = "chatroom_setting_id")
	@JsonIgnore
	private ChatroomSetting chatroomSetting;
}
