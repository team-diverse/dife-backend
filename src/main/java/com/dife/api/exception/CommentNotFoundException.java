package com.dife.api.exception;

public class CommentNotFoundException extends RuntimeException {
	public CommentNotFoundException() {
		super("해당 댓글을 찾을 수 없습니다!");
	}
}
