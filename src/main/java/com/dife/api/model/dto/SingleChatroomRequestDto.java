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
@Schema(description = "싱글 채팅방 생성 요청 객체")
public class SingleChatroomRequestDto {

	@Schema(description = "채팅방 생성 성공여부", example = "true")
	private Boolean success;

	@Schema(description = "채팅방 고유번호", example = "1")
	private Long roomId;

	public SingleChatroomRequestDto(Chatroom chatroom) {
		this.success = true;
		this.roomId = chatroom.getId();
	}
}
