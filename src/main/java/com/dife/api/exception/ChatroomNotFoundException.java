package com.dife.api.exception;

import java.io.IOException;

public class ChatroomNotFoundException extends IOException {
	public ChatroomNotFoundException() {
		super("존재하지 않는 채팅방입니다.");
	}
}
