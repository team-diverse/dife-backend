package com.dife.api.exception;

public class PostUnauthorizedException extends RuntimeException {
	public PostUnauthorizedException() {
		super("접근 권한이 없는 게시글입니다.");
	}
}
