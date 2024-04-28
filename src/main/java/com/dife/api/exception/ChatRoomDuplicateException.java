package com.dife.api.exception;

public class ChatRoomDuplicateException extends RuntimeException {
	public ChatRoomDuplicateException() {
		super("이미 해당 채팅방이름을 가진 채팅방이 존재합니다.");
	}
}
