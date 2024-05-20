package com.dife.api.model.dto;

import com.dife.api.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 응답 객체")
public class ChatroomResponseDto {

	@JsonProperty("room_id")
	@Schema(description = "채팅방 고유번호", example = "1")
	private Long roomId;

	@Schema(description = "채팅방 이름", example = "21학번 모여라")
	private String name;

	private ChatroomType chatroomType;

	@Schema(description = "채팅방 생성 성공여부", example = "true")
	private String description;

	@Schema(description = "채팅방 프로필 사진 파일명", example = "cookie")
	private String profile_img_name;

	@Schema(description = "채팅방 태그", example = "cookie")
	private Set<String> tags;

	@Schema(description = "채팅방 최대 인원수", example = "30")
	private Integer max_count;

	@Schema(description = "채팅방 설정 번역 언어", example = "Korean")
	private Set<String> languages;

	@Schema(description = "채팅방 목적", example = "communication, free")
	private Set<String> purposes;

	@Schema(description = "채팅방 공개/비공개 여부", example = "true")
	private Boolean is_public;

	@Schema(description = "채팅방 비밀번호", example = "00000")
	private String password;

	@Schema(description = "채팅방 생성 일시")
	private LocalDateTime created;

	public ChatroomResponseDto(Chatroom chatroom) {

		this.chatroomType = chatroom.getChatroomType();
		this.name = chatroom.getName();
		this.description = chatroom.getChatroom_setting().getDescription();
		this.profile_img_name = chatroom.getChatroom_setting().getProfile_img_name();
		this.roomId = chatroom.getId();
		if (chatroom.getChatroom_setting().getTags() != null
				&& !chatroom.getChatroom_setting().getTags().isEmpty()) {
			this.tags =
					chatroom.getChatroom_setting().getTags().stream()
							.map(Tag::getName)
							.collect(Collectors.toSet());
		}
		this.max_count = chatroom.getChatroom_setting().getMax_count();
		if (chatroom.getChatroom_setting().getLanguages() != null
				&& !chatroom.getChatroom_setting().getLanguages().isEmpty()) {
			this.languages =
					chatroom.getChatroom_setting().getLanguages().stream()
							.map(Language::getName)
							.collect(Collectors.toSet());
		}
		if (chatroom.getChatroom_setting().getPurposes() != null
				&& !chatroom.getChatroom_setting().getPurposes().isEmpty()) {
			this.purposes =
					chatroom.getChatroom_setting().getPurposes().stream()
							.map(GroupPurpose::getName)
							.collect(Collectors.toSet());
		}
		this.is_public = chatroom.getChatroom_setting().getIs_public();
		this.password = chatroom.getChatroom_setting().getPassword();
		this.created = chatroom.getCreated();
	}
}
