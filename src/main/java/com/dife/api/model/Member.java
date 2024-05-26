package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Entity
@Component
@Table(name = "member")
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull private String email;

	@NotNull
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$", message = "비밀번호는 영문자와 숫자를 포함해야 합니다.")
	private String password;

	@Column(unique = true)
	private String username;

	private String name;

	@Column(unique = true)
	private String student_id;

	private String major;

	private String role = "user";

	private String verification_file_id;

	private Boolean is_korean;

	private Boolean is_public = true;

	@Enumerated(EnumType.STRING)
	private MbtiCategory mbti;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private Set<Language> languages;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private Set<Hobby> hobbies;

	private String profile_file_id;

	@Size(max = 60, message = "자기소개는 최대 60자까지 입력 가능합니다.")
	private String bio;

	private Boolean is_verified = false;

	@JsonIgnore
	@OneToMany(mappedBy = "fromMember")
	private Set<Connect> sent;

	@JsonIgnore
	@OneToMany(mappedBy = "toMember")
	private Set<Connect> received;

	@ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
	@JsonBackReference
	private Set<Chatroom> chatrooms;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private Set<Bookmark> bookmarks;

	@JsonIgnore
	@OneToMany(mappedBy = "member")
	private Set<Post> posts;
}
