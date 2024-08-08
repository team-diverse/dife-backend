package com.dife.api.exception;

public class NotificationAuthorizationException extends RuntimeException {

	public NotificationAuthorizationException() {
		super("알림 편집 자격이 없습니다!");
	}
}
