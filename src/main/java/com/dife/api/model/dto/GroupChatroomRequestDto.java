package com.dife.api.model.dto;

import com.dife.api.model.Chatroom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "그룹 채팅방 생성 요청 객체")
public class GroupChatroomRequestDto {

	@Schema(description = "채팅방 생성 성공여부", example = "true")
	private Boolean success;

	@Schema(description = "채팅방 고유번호", example = "1")
	private Long roomId;

	@Schema(description = "그룹 채팅방 이름", example = "21학번 모여라")
	private String name;

	@Schema(description = "그룹 채팅방 한줄 소개", example = "21학번 정보 공유 및 잡담 채팅방")
	private String description;

	@Schema(description = "그룹 채팅방 프로필 사진 파일명", example = "cookie")
	private String profile_img_name;

	public GroupChatroomRequestDto(Chatroom chatroom) {
		this.success = true;
		this.roomId = chatroom.getId();
		this.name = chatroom.getName();
		this.description = chatroom.getSetting().getDescription();
		this.profile_img_name = chatroom.getSetting().getProfile_img_name();
	}
}
