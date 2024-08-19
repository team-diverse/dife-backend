package com.dife.api.model.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlockMemberResponseDto {
	private Long id;
	private Long memberId;
	private Long blacklistedMemberId;
	private LocalDateTime modified;
}
