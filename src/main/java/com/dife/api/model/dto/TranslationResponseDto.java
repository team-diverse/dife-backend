package com.dife.api.model.dto;

import com.dife.api.model.Translation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResponseDto {

	private List<Translation> translations;
}
