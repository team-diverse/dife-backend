package com.dife.api.exception;

public class BlockDuplicateException extends RuntimeException {
	public BlockDuplicateException() {
		super("이미 차단 처리된 상태입니다!");
	}
}
