package com.dife.api.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {
	private Long id;

	@NotNull
	@Size(max = 300)
	private String message;

	private MemberRestrictedResponseDto member;

	private GroupChatroomResponseDto groupChatroom;
	private SingleChatroomResponseDto singleChatroom;

	private List<String> imgCode;
	private LocalDateTime created;
}
