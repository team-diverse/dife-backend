package com.dife.api.exception;

public class TranslationFullException extends IllegalArgumentException {

	public TranslationFullException() {
		super("번역은 한 ID당 15번만 무료로 사용 가능합니다!");
	}
}
