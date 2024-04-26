package com.dife.api.exception;

public class ConnectNotFoundException extends RuntimeException {
	public ConnectNotFoundException() {
		super("해당 커넥트는 존재하지 않습니다.");
	}
}
