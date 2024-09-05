package com.dife.api.exception;

public class ChatNotFoundException extends IllegalStateException {
	public ChatNotFoundException() {
		super("해당 채팅은 존재하지 않습니다.");
	}
}
