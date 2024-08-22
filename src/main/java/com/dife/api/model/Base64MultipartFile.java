package com.dife.api.model;

import java.io.*;
import java.io.File;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
public class Base64MultipartFile implements MultipartFile {

	private final byte[] fileContent;
	private final String fileName;
	private final String contentType;

	public Base64MultipartFile(byte[] fileContent, String fileName, String contentType)
			throws IOException {
		this.fileName = fileName;
		this.fileContent = fileContent;
		this.contentType = contentType;
	}

	@Override
	public String getName() {
		return this.fileName;
	}

	@Override
	public String getOriginalFilename() {
		return this.fileName;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public boolean isEmpty() {
		return this.fileContent.length == 0;
	}

	@Override
	public long getSize() {
		return this.fileContent.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return this.fileContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.fileContent);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		try (OutputStream out = new FileOutputStream(dest)) {
			out.write(this.fileContent);
		}
	}
}
