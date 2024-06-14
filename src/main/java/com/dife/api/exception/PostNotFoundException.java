package com.dife.api.exception;

public class PostNotFoundException extends RuntimeException {
	public PostNotFoundException() {
		super("해당 게시물을 찾을 수 없습니다!");
	}
}
