package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.*;
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

	private String verifyCode = "";

	private String username = "Diver";

	private String name = "";

	private String studentId = "";

	private String major = "";

	private String role = "user";

	@OneToOne private File verificationFile;

	private String country = "KO";

	private Boolean isPublic = true;

	@Enumerated(EnumType.STRING)
	private MbtiCategory mbti;

	private String settingLanguage = "EN";

	private Integer translationCount = 0;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Language> languages = new HashSet<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Hobby> hobbies = new HashSet<>();

	@OneToOne private File profileImg;

	@Size(max = 60, message = "자기소개는 최대 60자까지 입력 가능합니다.")
	private String bio = "";

	private Boolean isVerified = false;

	private Boolean isDeleted = false;

	@JsonIgnore
	@OneToMany(mappedBy = "fromMember", cascade = CascadeType.ALL)
	private Set<Connect> sent;

	@JsonIgnore
	@OneToMany(mappedBy = "toMember", cascade = CascadeType.ALL)
	private Set<Connect> received;

	@ManyToMany(mappedBy = "members")
	@JsonIgnore
	private Set<Chatroom> chatrooms = new HashSet<>();

	@OneToMany(mappedBy = "manager")
	@JsonIgnore
	private Set<Chatroom> managingChatrooms = new HashSet<>();

	@OneToMany(mappedBy = "member")
	@JsonIgnore
	private List<Chat> chats;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Bookmark> bookmarks;

	@OneToMany(mappedBy = "writer")
	@JsonIgnore
	private Set<Post> posts;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<PostLike> postLikes;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<NotificationToken> notificationTokens;

	@OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Report> reports;

	@OneToMany(mappedBy = "member")
	@JsonIgnore
	private Set<PostBlock> postBlocks = new HashSet<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<MemberBlock> blackList = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "member_likelist",
			joinColumns = @JoinColumn(name = "member_id"),
			inverseJoinColumns = @JoinColumn(name = "likelisted_member_id"))
	@JsonIgnore
	private List<Member> likeList = new ArrayList<>();
}
