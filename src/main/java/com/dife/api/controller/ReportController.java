package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.model.dto.ReportRequestDto;
import com.dife.api.service.ReportService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController implements SwaggerReportController {

	private final ReportService reportService;

	@PostMapping
	public ResponseEntity<Void> createDeclaration(
			@RequestBody ReportRequestDto requestDto, Authentication auth) throws IOException {

		reportService.createReport(requestDto, auth.getName());
		return new ResponseEntity<>(CREATED);
	}
}
