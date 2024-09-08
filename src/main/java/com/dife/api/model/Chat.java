package com.dife.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "chat")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chat implements TranslateTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 300)
	private String message;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chatroom_id")
	@JsonIgnore
	private Chatroom chatroom;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ElementCollection private List<String> imgCode = new ArrayList<>();

	private LocalDateTime created;

	@Override
	public String getTextToTranslate() {
		return message;
	}

	@Override
	public String getTitleToTranslate() {
		return null;
	}
}
