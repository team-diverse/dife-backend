package com.dife.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

	@NotNull private String title;

	@NotNull private String content;

	@NotNull
	@Enumerated(EnumType.STRING)
	private BoardCategory boardType;

	private Integer viewCount;

	@NotNull private Boolean is_public;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
}
