package com.dife.api.exception;

public class ChatroomDuplicateException extends RuntimeException {
	public ChatroomDuplicateException() {
		super("이미 해당 채팅방이름을 가진 채팅방이 존재합니다.");
	}
}
