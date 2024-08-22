package com.dife.api.model;

import java.io.*;
import java.io.File;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {

	private final DiskFileItem fileItem;
	private final byte[] fileContent;

	public CustomMultipartFile(DiskFileItem fileItem) throws IOException {
		this.fileItem = fileItem;
		this.fileContent = IOUtils.toByteArray(fileItem.getInputStream());
	}

	@Override
	public String getName() {
		return fileItem.getFieldName();
	}

	@Override
	public String getOriginalFilename() {
		return fileItem.getName();
	}

	@Override
	public String getContentType() {
		return fileItem.getContentType();
	}

	@Override
	public boolean isEmpty() {
		return fileItem.getSize() == 0;
	}

	@Override
	public long getSize() {
		return fileItem.getSize();
	}

	@Override
	public byte[] getBytes() throws IOException {
		return fileContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(fileContent);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		try (OutputStream out = new FileOutputStream(dest)) {
			out.write(fileContent);
		}
	}
}
