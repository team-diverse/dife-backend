package com.dife.api.controller;

import com.dife.api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController implements SwaggerFileController {

	private final FileService fileService;

	@GetMapping("/{id}")
	public ResponseEntity<String> getFile(@PathVariable("id") Long id, Authentication auth) {
		return ResponseEntity.ok(fileService.getPresignUrl(id, auth.getName()));
	}

	@GetMapping
	public ResponseEntity<String> getFile(@RequestParam("name") String name) {
		return ResponseEntity.ok(fileService.getPresignUrl(name));
	}
}
