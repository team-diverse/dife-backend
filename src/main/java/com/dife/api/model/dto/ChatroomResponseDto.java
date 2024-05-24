package com.dife.api.model.dto;

import com.dife.api.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 응답 객체")
public class ChatroomResponseDto {

	private Long id;

	private String name;

	private ChatroomType chatroomType;

	private String description;

	private Map<String, String> activeSessions;

	private String profileImgName;

	private Set<String> tags;

	private Integer count;

	private Integer maxCount;

	private Set<String> languages;

	private Set<String> purposes;

	private Boolean isPublic;

	private String password;

	private LocalDateTime created;

	@JsonProperty("member")
	private Set<Member> Members;

	@JsonProperty("chat")
	private Set<Chat> chats;
}
