package com.dife.api.model.dto;

import com.dife.api.model.Chatroom;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "그룹 채팅방 응답 객체")
public class GroupChatroomDto {

	@Schema(description = "그룹 채팅방 생성 성공여부", example = "true")
	private Boolean success;

	private Long roomId;

	@NotNull(message = "그룹 채팅방 이름을 입력해주세요!")
	@Schema(description = "그룹 채팅방 이름", example = "21학번 모여라")
	private String name;

	@NotNull(message = "그룹 채팅방의 한줄 소개를 입력해주세요!")
	@Schema(description = "그룹 채팅방 한줄 소개", example = "21학번 정보 공유 및 잡담 채팅방")
	private String description;

	@Schema(description = "채팅방 생성 일시")
	private LocalDateTime created;

	public GroupChatroomDto(Chatroom chatroom) {
		this.success = true;
		this.roomId = chatroom.getId();
		this.name = chatroom.getName();
		this.description = chatroom.getSetting().getDescription();
		this.created = chatroom.getCreated();
	}
}
