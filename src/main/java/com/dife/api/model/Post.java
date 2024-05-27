package com.dife.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String content;

	private Boolean isPublic;

	@Enumerated(EnumType.STRING)
	private BoardCategory boardType = BoardCategory.FREE;

	private Integer viewCount = 0;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
}
