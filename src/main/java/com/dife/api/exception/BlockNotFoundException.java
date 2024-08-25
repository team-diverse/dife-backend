package com.dife.api.exception;

public class BlockNotFoundException extends IllegalStateException {
	public BlockNotFoundException() {
		super("블랙리스트에서 찾을 수 없는 회원입니다!");
	}
}
