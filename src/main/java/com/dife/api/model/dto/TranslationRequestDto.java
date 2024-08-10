package com.dife.api.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationRequestDto {

	private List<String> text;
	private String targetLang;
	private Long bookmarkId;
}
