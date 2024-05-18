package com.dife.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chatroom_setting")
public class ChatroomSetting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(max = 60)
	private String description;

	private String profile_img_name;

	@OneToMany(mappedBy = "chatroom_setting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Tag> tags;

	private Integer count = 0;
	private Integer max_count;

	@OneToMany(mappedBy = "chatroom_setting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<GroupPurpose> purposes;

	@OneToMany(mappedBy = "chatroom_setting", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Language> languages;

	private Boolean is_public;

	@Size(min = 5, max = 5, message = "비밀번호는 정확히 5자 이어야 합니다.")
	@Pattern(regexp = "^[0-9]{5}$", message = "비밀번호는 숫자 5자로 구성되어야 합니다.")
	private String password;
}
