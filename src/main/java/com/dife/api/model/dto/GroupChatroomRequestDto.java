package com.dife.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

	@NotNull(message = "그룹 채팅방 이름을 입력해주세요!")
	@Schema(description = "그룹 채팅방 이름", example = "21학번 모여라")
	private String name;

	@NotNull(message = "그룹 채팅방의 한줄 소개를 입력해주세요!")
	@Schema(description = "그룹 채팅방 한줄 소개", example = "21학번 정보 공유 및 잡담 채팅방")
	private String description;

	@NotNull(message = "그룹 채팅방의 최대 인원수를 입력해주세요!")
	@Max(value = 30, message = "최소 인원수는 30명 이하여야 합니다.")
	@Schema(description = "그룹 채팅방 최대 인원수", example = "30")
	private Integer max_count;

	@NotNull(message = "그룹 채팅방의 최소 인원수를 입력해주세요!")
	@Min(value = 3, message = "최소 인원수는 3명 이상이어야 합니다.")
	@Schema(description = "그룹 채팅방 최소 인원수", example = "3")
	private Integer min_count;
}
