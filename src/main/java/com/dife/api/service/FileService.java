package com.dife.api.service;

import com.dife.api.model.File;
import com.dife.api.model.Format;
import com.dife.api.model.dto.FileDto;
import com.dife.api.repository.FileRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
	private final FileRepository fileRepository;
	private final S3Client s3Client;
	private final ModelMapper modelMapper;

	@Value("${spring.aws.bucket-name}")
	private String bucketName;

	public FileDto upload(MultipartFile file) {
		if (file.isEmpty()) {
			throw new RuntimeException("Empty file cannot be uploaded");
		}

		String originalFilename = file.getOriginalFilename();
		String fileName = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
		Long fileSize = file.getSize();

		if (fileSize > 5_000_000) {
			throw new RuntimeException("File size exceeds limit of 5MB");
		}

		try {
			PutObjectRequest putObjectRequest =
					PutObjectRequest.builder().bucket(bucketName).key(originalFilename).build();
			s3Client.putObject(
					putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload file to Server:", e);
		}

		File fileInfo = new File();
		fileInfo.setOriginalName(originalFilename);
		fileInfo.setName(fileName);
		fileInfo.setSize(fileSize);
		fileInfo.setUrl("https://");
		fileInfo.setFormat(Format.JPG);
		fileRepository.save(fileInfo);

		return modelMapper.map(fileInfo, FileDto.class);
	}

	public String getImageUrl(String fileName) {
		return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
	}
}
