package com.dife.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chatroom_setting")
public class ChatroomSetting extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 60)
	private String description = "";

	@OneToOne private File profileImg;

	@OneToMany(mappedBy = "chatroomSetting", cascade = CascadeType.ALL)
	private Set<Hobby> hobbies = new HashSet<>();

	private Integer count = 0;

	private Integer maxCount = 2;

	@OneToMany(mappedBy = "chatroomSetting", cascade = CascadeType.ALL)
	private Set<GroupPurpose> purposes = new HashSet<>();

	@OneToMany(mappedBy = "chatroomSetting", cascade = CascadeType.ALL)
	private Set<Language> languages = new HashSet<>();

	private Boolean isPublic;

	@Size(min = 5, max = 5, message = "비밀번호는 정확히 5자 이어야 합니다.")
	@Pattern(regexp = "^[0-9]{5}$", message = "비밀번호는 숫자 5자로 구성되어야 합니다.")
	private String password = "00000";
}
