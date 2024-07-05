package com.dife.api.exception;

public class NotificationException extends RuntimeException {

	public NotificationException() {
		super("알림 전송에 문제가 있습니다!");
	}
}
