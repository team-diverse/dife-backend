package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	private Boolean isPublic = true;

	private Integer likeCount = 0;

	private Integer viewCount = 0;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "writer_id")
	private Member writer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;
}
