package com.dife.api.exception;

public class CommentDuplicateException extends RuntimeException {
	public CommentDuplicateException() {
		super("사용자당 한 개의 게시물에 대해 한 댓글만 가능합니다!");
	}
}
