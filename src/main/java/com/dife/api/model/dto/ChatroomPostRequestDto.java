package com.dife.api.model.dto;

import com.dife.api.model.ChatroomType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatroomPostRequestDto {

	private String name;

	private String description;

	private ChatroomType chatroomType;

	private Long memberId;
}
