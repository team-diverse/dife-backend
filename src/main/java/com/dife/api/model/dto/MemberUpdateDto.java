package com.dife.api.model.dto;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

	private String password;

	private String username;

	private Boolean is_public;

	private Set<String> languages;

	private Set<String> hobbies;

	private String profile_file_id;

	private String bio;
}
