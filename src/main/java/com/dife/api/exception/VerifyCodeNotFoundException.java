package com.dife.api.exception;

public class VerifyCodeNotFoundException extends IllegalStateException {
	public VerifyCodeNotFoundException() {
		super("비밀번호 변경 메일을 보낸 후에 접근해주세요!");
	}
}
