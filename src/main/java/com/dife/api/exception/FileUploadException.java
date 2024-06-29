package com.dife.api.exception;

public class FileUploadException extends IllegalStateException {
	public FileUploadException() {
		super("파일 업로드 실패");
	}
}
