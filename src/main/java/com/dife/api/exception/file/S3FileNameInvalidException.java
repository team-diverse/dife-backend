package com.dife.api.exception.file;

public class S3FileNameInvalidException extends RuntimeException {
	public S3FileNameInvalidException(String message) {
		super(message);
	}
}
