package com.dife.api.controller;

import com.dife.api.model.dto.TranslationRequestDto;
import com.dife.api.model.dto.TranslationResponseDto;
import com.dife.api.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/translations")
@Slf4j
public class TranslationController implements SwaggerTranslationController {

	private final TranslationService translationService;

	@PostMapping
	public ResponseEntity<TranslationResponseDto> translate(
			@RequestBody TranslationRequestDto requestDto, Authentication auth) {

		TranslationResponseDto response = translationService.translate(requestDto, auth.getName());

		return ResponseEntity.ok(response);
	}
}
