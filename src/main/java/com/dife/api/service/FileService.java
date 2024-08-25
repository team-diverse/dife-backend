package com.dife.api.service;

import com.dife.api.exception.file.S3FileNameInvalidException;
import com.dife.api.exception.file.S3FileNotFoundException;
import com.dife.api.model.File;
import com.dife.api.model.Format;
import com.dife.api.model.dto.FileDto;
import com.dife.api.repository.FileRepository;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
	private final FileRepository fileRepository;
	private final S3Client s3Client;
	private final S3Presigner presigner;
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

		if (fileSize > 10_000_000) {
			throw new RuntimeException("File size exceeds limit of 10MB");
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
		fileInfo.setFormat(Format.JPG);
		fileRepository.save(fileInfo);

		return modelMapper.map(fileInfo, FileDto.class);
	}

	public String getPresignUrl(String fileName) {
		if (fileName == null || fileName.trim().isEmpty()) {
			throw new S3FileNameInvalidException("파일 이름이 비어있습니다.");
		}
		return generatePresignedUrl(fileName);
	}

	public String getPresignUrl(Long fileId) {
		File file = fileRepository.findById(fileId).orElseThrow(S3FileNotFoundException::new);
		return generatePresignedUrl(file.getName());
	}

	public String generatePresignedUrl(String fileName) {
		GetObjectRequest getObjectRequest =
				GetObjectRequest.builder().bucket(bucketName).key(fileName).build();

		GetObjectPresignRequest getObjectPresignRequest =
				GetObjectPresignRequest.builder()
						.signatureDuration(Duration.ofMinutes(10))
						.getObjectRequest(getObjectRequest)
						.build();

		PresignedGetObjectRequest presignedGetObjectRequest =
				presigner.presignGetObject(getObjectPresignRequest);

		String url = presignedGetObjectRequest.url().toString();
		presigner.close();
		return url;
	}

	public void deleteFile(Long id) {
		File file = fileRepository.getReferenceById(id);
		fileRepository.delete(file);
		DeleteObjectRequest deleteObjectRequest =
				DeleteObjectRequest.builder().bucket(bucketName).key(file.getOriginalName()).build();

		s3Client.deleteObject(deleteObjectRequest);
	}
}
