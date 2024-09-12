package com.dife.api.model.dto;

import com.dife.api.model.ConnectStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConnectResponseDto {
	private Long id;

	@NotNull
	@JsonProperty("from_member")
	@Schema(description = "커넥트 요청 받는 회원의 회원정보", example = "Member엔티티 참고")
	private MemberRestrictedResponseDto fromMember;

	@NotNull
	@JsonProperty("to_member")
	@Schema(description = "커넥트 요청 보내는 회원의 회원정보", example = "Member엔티티 참고")
	private MemberRestrictedResponseDto toMember;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Schema(description = "커넥트 요청 상태", example = "PENDING")
	private ConnectStatus status;

	@Schema(description = "커넥트 요청 생성 일시")
	private LocalDateTime created;
}
