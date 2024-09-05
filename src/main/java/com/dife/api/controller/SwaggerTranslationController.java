package com.dife.api.controller;

import com.dife.api.model.dto.TranslationRequestDto;
import com.dife.api.model.dto.TranslationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Translation API", description = "DEEPL API를 이용한 번역 서비스 API")
public interface SwaggerTranslationController {

	@Operation(
			summary = "번역 생성 API",
			description = "text 값에 번역하고자 하는 내용을 적어줍니다. 번역본 언어는 target_lang에 적어줍니다.")
	@ApiResponse(
			responseCode = "200",
			description = "번역 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = TranslationResponseDto.class))
			})
	ResponseEntity<TranslationResponseDto> translate(
			@RequestBody TranslationRequestDto requestDto, Authentication auth);
}
