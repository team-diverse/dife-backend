package com.dife.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConnectRequestDto {

	@NotNull
	@JsonProperty("to_member_id")
	private Long toMemberId;
}
