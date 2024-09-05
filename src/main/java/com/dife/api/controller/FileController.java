package com.dife.api.controller;

import com.dife.api.service.FileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController implements SwaggerFileController {

	private final FileService fileService;

	@GetMapping
	public ResponseEntity<String> getFile(@RequestParam(name = "fileName") String fileName)
			throws IOException {
		return ResponseEntity.ok(fileService.getPresignUrl(fileName));
	}
}
