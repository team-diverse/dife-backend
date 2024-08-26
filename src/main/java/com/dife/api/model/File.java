package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file")
public class File extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String originalName;

	private String name;

	private Boolean isSecret = false;

	private Long size;

	@Enumerated(EnumType.STRING)
	private Format format;

	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;

	@ManyToOne
	@JoinColumn(name = "chat_id")
	@JsonIgnore
	private Chat chat;
}
