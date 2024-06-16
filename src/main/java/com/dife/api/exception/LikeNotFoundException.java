package com.dife.api.exception;

public class LikeNotFoundException extends IllegalStateException {
	public LikeNotFoundException() {
		super("해당 좋아요는 존재하지 않습니다.");
	}
}
