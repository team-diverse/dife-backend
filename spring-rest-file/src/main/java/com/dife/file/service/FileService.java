package com.dife.file.service;

import com.dife.file.model.File;
import com.dife.file.model.Format;
import com.dife.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final FileRepository fileRepository;

    public void upload(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("Empty file cannot be uploaded");
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        Long fileSize = file.getSize();

        if (fileSize > 5_000_000) {
            throw new RuntimeException("File size exceeds limit of 5MB");
        }

        Path rootLocation = Paths.get("uploads");
        Path fileDestination = rootLocation.resolve(Paths.get(originalFilename)).normalize().toAbsolutePath();
        try {
            Files.createDirectories(rootLocation);
            file.transferTo(fileDestination);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file locally.");
        }

        File fileInfo = new File();
        fileInfo.setName(fileName);
        fileInfo.setSize(fileSize);
        fileInfo.setUrl("https://");
        fileInfo.setFormat(Format.JPG);

        fileRepository.save(fileInfo);
    }
}
