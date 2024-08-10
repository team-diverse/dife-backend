package com.dife.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookmark")
public class Bookmark extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 300)
	private String message;

	@ManyToMany
	@JoinTable(
			name = "bookmark_translation",
			joinColumns = @JoinColumn(name = "bookmark_id"),
			inverseJoinColumns = @JoinColumn(name = "translation_id"))
	private List<Translation> translations;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	private LocalDateTime created;
}
