package com.dife.api.exception;

public class IdenticalConnectException extends IllegalArgumentException {
	public IdenticalConnectException() {
		super("본인에게 커넥트를 보낼 수 없습니다.");
	}
}
