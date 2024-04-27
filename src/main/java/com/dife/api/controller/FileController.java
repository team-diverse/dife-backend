package com.dife.api.controller;

import com.dife.api.model.dto.FileDto;
import com.dife.api.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/")
    ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile file) {
        FileDto dto = fileService.upload(file);
        return ResponseEntity.status(CREATED).body(dto);
    }
}
