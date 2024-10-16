package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity implements TranslateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title = "";

	@Column(columnDefinition = "LONGTEXT")
	private String content;

	private Boolean isPublic = true;

	@Enumerated(EnumType.STRING)
	private BoardCategory boardType = BoardCategory.FREE;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member writer;

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Comment> comments;

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<PostLike> postLikes;

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Bookmark> bookmarks;

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JsonIgnore
	private List<File> files = new ArrayList<>();

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Report> reports;

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<PostBlock> postBlocks;

	@Override
	public String getTextToTranslate() {
		return content;
	}

	@Override
	public String getTitleToTranslate() {
		return title;
	}
}
