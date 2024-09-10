package com.dife.api.model.dto;

import com.dife.api.model.*;
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
@Schema(description = "싱글 채팅방 응답 객체")
public class SingleChatroomResponseDto implements ChatroomResponseDto {

	private Long id;

	private LocalDateTime created;

	@JsonProperty("chatroom_type")
	private ChatroomType chatroomType;

	private Set<MemberRestrictedResponseDto> members = new HashSet<>();
}
