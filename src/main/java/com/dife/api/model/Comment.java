package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment extends BaseTimeEntity implements TranslateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	private Boolean isPublic = true;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "writer_id")
	private Member writer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private Comment parentComment;

	@OneToMany(mappedBy = "parentComment")
	@JsonBackReference
	private List<Comment> childrenComments = new ArrayList<>();

	@OneToMany(mappedBy = "comment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<CommentLike> CommentLikes;

	@OneToMany(mappedBy = "comment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Report> reports;

	@Override
	public String getTextToTranslate() {
		return content;
	}

	@Override
	public String getTitleToTranslate() {
		return null;
	}
}
