package com.dife.api.model.dto;

import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatroomPutRequestDto {

	private String profileImgName;

	@Size(max = 30)
	private Integer maxCount;

	private Set<String> purposes;

	private Set<String> tags;

	private Set<String> languages;

	private Boolean isPublic;

	private String password;
}
