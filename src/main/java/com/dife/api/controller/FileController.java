package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.FileLocation;
import com.dife.api.model.dto.FileDto;
import com.dife.api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController implements SwaggerFileController {

	private final FileService fileService;

	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<FileDto> uploadFile(
			@RequestParam(name = "fileLocation") FileLocation fileLocation,
			@RequestParam(name = "id") Long id,
			@RequestParam(name = "file") MultipartFile file,
			Authentication auth) {
		FileDto dto = fileService.uploadFileLocation(file, fileLocation, id, auth.getName());
		return ResponseEntity.status(CREATED).body(dto);
	}

	@GetMapping
	public ResponseEntity<String> getFile(@RequestParam(name = "fileName") String fileName) {
		return ResponseEntity.ok(fileService.getPresignUrl(fileName));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFile(
			@PathVariable(name = "id") Long id,
			@RequestParam(name = "fileLocation") FileLocation fileLocation,
			Authentication auth) {
		fileService.deleteFileLocation(id, fileLocation, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
