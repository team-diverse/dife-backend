package com.dife.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post_likes")
public class LikePost extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
}
