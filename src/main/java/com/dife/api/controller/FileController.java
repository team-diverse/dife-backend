package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.dife.api.model.dto.FileDto;
import com.dife.api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController implements SwaggerFileController {

	private final FileService fileService;

	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<FileDto> uploadFile(@RequestParam(name = "file") MultipartFile file) {
		FileDto dto = fileService.upload(file);
		return ResponseEntity.status(CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<String> getFile(@RequestParam(name = "fileName") String fileName) {
		return ResponseEntity.ok(fileService.getPresignUrl(fileName));
	}
}
