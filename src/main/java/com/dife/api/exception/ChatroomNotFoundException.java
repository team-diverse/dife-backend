package com.dife.api.exception;

public class ChatroomNotFoundException extends IllegalStateException {
	public ChatroomNotFoundException() {
		super("해당 채팅방은 존재하지 않습니다.");
	}
}
