package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bookmark extends BaseTimeEntity implements TranslateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 300)
	private String message;

	@ManyToMany(cascade = CascadeType.ALL)
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

	@Override
	public String getTextToTranslate() {
		return message;
	}

	@Override
	public String getTitleToTranslate() {
		return null;
	}
}
