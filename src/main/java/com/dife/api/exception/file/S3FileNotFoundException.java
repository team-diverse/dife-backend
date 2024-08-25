package com.dife.api.exception.file;

public class S3FileNotFoundException extends RuntimeException {
	public S3FileNotFoundException() {
		super("해당 파일은 존재하지 않습니다.");
	}
}
