package com.dife.api.exception;

public class MemberNotAddVerificationException extends MemberException {

	public MemberNotAddVerificationException() {
		super("학생증 인증을 첨부해주세요!");
	}
}
