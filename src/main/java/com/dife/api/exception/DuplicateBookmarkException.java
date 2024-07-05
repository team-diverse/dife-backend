package com.dife.api.exception;

public class DuplicateBookmarkException extends DuplicateException {

	public DuplicateBookmarkException() {
		super("이미 북마크를 눌렀습니다!");
	}
}
