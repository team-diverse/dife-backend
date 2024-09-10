package com.dife.api.model.dto;

import com.dife.api.model.ChatroomType;
import com.dife.api.model.File;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "그룹 채팅방 응답 객체")
public class GroupChatroomResponseDto implements ChatroomResponseDto {

	private Long id;
	private LocalDateTime created;

	@JsonProperty("chatroom_type")
	private ChatroomType chatroomType;

	private Set<MemberRestrictedResponseDto> members = new HashSet<>();

	private MemberRestrictedResponseDto manager;

	private String name;

	private String description;

	private File profileImg;

	private Set<String> hobbies;

	private Integer count;

	private Integer maxCount;

	private Set<String> languages;

	private Set<String> purposes;

	private Boolean isPublic;

	private Boolean isLiked = false;

	private LocalDateTime modified;

	private Boolean isEntered;
}
