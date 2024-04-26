package com.dife.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConnectPatchRequestDto {

	@NotNull
	@JsonProperty("member_id")
	private Long memberId;
}
