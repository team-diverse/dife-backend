package com.dife.api.service;

import com.dife.api.exception.MemberException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class ImageService {

    @Value("${file.path}")
    private String imageUploadDir;

    public String uploadImage(MultipartFile file) {
        Path uploadPath = Paths.get(imageUploadDir);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new MemberException("이미지 디렉토리를 생성할 수 없습니다.");
            }
        }

        String imageName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path imagePath = uploadPath.resolve(imageName);

        try {
            Files.copy(file.getInputStream(), imagePath);
        } catch (IOException e) {
            throw new MemberException("이미지를 저장할 수 없습니다.");
        }
        return imagePath.toString();
    }
}
