package com.dife.member.controller;

import com.dife.member.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/")
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.upload(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Uploaded");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
}
