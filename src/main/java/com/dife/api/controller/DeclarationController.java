package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.model.dto.DeclarationRequestDto;
import com.dife.api.model.dto.DeclarationResponseDto;
import com.dife.api.service.DeclarationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/declarations")
public class DeclarationController {

	private final DeclarationService declarationService;

	@PostMapping
	public ResponseEntity<DeclarationResponseDto> createDeclaration(
			@RequestBody DeclarationRequestDto requestDto, Authentication auth) {

		DeclarationResponseDto responseDto =
				declarationService.createDeclaration(requestDto, auth.getName());
		return ResponseEntity.status(CREATED).body(responseDto);
	}
}
