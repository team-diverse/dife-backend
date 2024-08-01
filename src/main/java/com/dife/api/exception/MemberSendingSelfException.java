package com.dife.api.exception;

public class MemberSendingSelfException extends MemberException {

	public MemberSendingSelfException() {
		super("수신자를 본인에게로는 불가합니다!");
	}
}
