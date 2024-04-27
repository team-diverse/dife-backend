package com.dife.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExceptionResonse {

	private final Boolean success;
	private final String message;
}
