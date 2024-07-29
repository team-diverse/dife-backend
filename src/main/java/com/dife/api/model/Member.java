package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
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

	@NotNull private String password = "";

	private String username = "";

	private String name = "";

	private String studentId = "";

	private String major = "";

	private String role = "user";

	@OneToOne private File verificationFile;

	private Boolean isKorean = true;

	private Boolean isPublic = true;

	@Enumerated(EnumType.STRING)
	private MbtiCategory mbti;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private Set<Language> languages = new HashSet<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	private Set<Hobby> hobbies = new HashSet<>();

	@OneToOne private File profileImg;

	@Size(max = 60, message = "자기소개는 최대 60자까지 입력 가능합니다.")
	private String bio = "";

	private Boolean isVerified = false;

	@JsonIgnore
	@OneToMany(mappedBy = "fromMember")
	private Set<Connect> sent;

	@JsonIgnore
	@OneToMany(mappedBy = "toMember")
	private Set<Connect> received;

	@ManyToMany(mappedBy = "members")
	@JsonIgnore
	private Set<Chatroom> chatrooms = new HashSet<>();

	@OneToMany(mappedBy = "member")
	@JsonIgnore
	private List<Chat> chats;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Bookmark> bookmarks;

	@OneToMany(mappedBy = "writer")
	@JsonIgnore
	private Set<Post> posts;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<PostLike> PostLikes;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<NotificationToken> notificationTokens;

	@OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Report> reports;

	@OneToMany(mappedBy = "blacklistOwner", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<BlacklistedMember> blackList;
}
