package com.dife.api.exception;

public class MemberNotAddVerificationException extends MemberException {

	public MemberNotAddVerificationException() {
		super("해당 멤버는 학생증 인증을 첨부하지 않았습니다!");
	}
}
