package com.dife.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Report")
public class Report extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private ReportType type;

	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comment_id")
	private Comment comment;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "receiver_id")
	private Member receiver;
}
