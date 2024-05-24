package com.dife.api.exception;

public class BookmarkNotFoundException extends RuntimeException {
	public BookmarkNotFoundException() {
		super("해당 북마크는 존재하지 않습니다.");
	}
}
