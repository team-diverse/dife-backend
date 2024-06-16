package com.dife.api.exception;

public class DuplicateLikeException extends DuplicateException {

	public DuplicateLikeException() {
		super("이미 좋아요를 눌렀습니다!");
	}
}
