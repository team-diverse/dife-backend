package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.model.dto.ReportRequestDto;
import com.dife.api.model.dto.ReportResponseDto;
import com.dife.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;

	@PostMapping
	public ResponseEntity<ReportResponseDto> createDeclaration(
			@RequestBody ReportRequestDto requestDto, Authentication auth) {

		ReportResponseDto responseDto = reportService.createDeclaration(requestDto, auth.getName());
		return ResponseEntity.status(CREATED).body(responseDto);
	}
}
