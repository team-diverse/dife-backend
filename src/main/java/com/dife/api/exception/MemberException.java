package com.dife.api.exception;

public class MemberException extends IllegalArgumentException {

	public MemberException(String message) {
		super(message);
	}

	public MemberException(Throwable cause) {
		super(cause);
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);
	}
}
