package com.dife.api.exception;

public class ChatroomCountException extends IllegalArgumentException {

	public ChatroomCountException() {
		super("그룹 채팅방의 최소 인원은 3명, 최대 인원은 30명입니다.");
	}
}
