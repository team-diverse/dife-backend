package com.dife.api.exception;

public class SingleChatroomCreateDuplicateException extends RuntimeException {
	public SingleChatroomCreateDuplicateException() {
		super("이미 유저 사이에 채팅방이 존재합니다.");
	}
}
