package com.dife.api.model.dto;

import com.dife.api.model.ChatroomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 닉네임 응답 객체")
public class ChatroomTypeRequestDto {

	private ChatroomType chatroomType;
}
