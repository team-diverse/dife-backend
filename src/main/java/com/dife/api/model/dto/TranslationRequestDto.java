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
	private String target_lang;
	private Long postId;
	private Long commentId;
	private Long chatId;
	private Long bookmarkId;
}
