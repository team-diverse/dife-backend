package com.dife.api;

import static org.springframework.http.HttpStatus.*;

import com.dife.api.exception.*;
import com.dife.api.exception.file.S3FileNameInvalidException;
import com.dife.api.exception.file.S3FileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<ExceptionResonse> handleMemberException(MemberException exception) {
		return ResponseEntity.status(FORBIDDEN.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ExceptionResonse> handleDuplicateException(DuplicateException exception) {
		return ResponseEntity.status(CONFLICT.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResonse> handleBadCredentialsException(
			BadCredentialsException exception) {
		return ResponseEntity.status(UNAUTHORIZED.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResonse> handleException(Exception exception) {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(RegisterException.class)
	public ResponseEntity<ExceptionResonse> handleRegisterException(RegisterException exception) {
		return ResponseEntity.status(UNPROCESSABLE_ENTITY.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResonse> handleIllegalArgumentException(
			IllegalArgumentException exception) {
		return ResponseEntity.status(FORBIDDEN.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(MemberNullException.class)
	public ResponseEntity<ExceptionResonse> handleNullPointerException(
			NullPointerException exception) {
		return ResponseEntity.status(FORBIDDEN.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(ConnectDuplicateException.class)
	public ResponseEntity<ExceptionResonse> handleConnectException(
			ConnectDuplicateException exception) {
		return ResponseEntity.status(CONFLICT)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(BlockNotFoundException.class)
	public ResponseEntity<ExceptionResonse> handleBlockException(BlockNotFoundException exception) {
		return ResponseEntity.status(NOT_FOUND.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<ExceptionResonse> handlePostNotFoundException(
			PostNotFoundException exception) {
		return ResponseEntity.status(FORBIDDEN.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(ConnectNotFoundException.class)
	public ResponseEntity<ExceptionResonse> handleConnectException(
			ConnectNotFoundException exception) {
		return ResponseEntity.status(NOT_FOUND)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(ConnectUnauthorizedException.class)
	public ResponseEntity<ExceptionResonse> handleConnectException(
			ConnectUnauthorizedException exception) {
		return ResponseEntity.status(FORBIDDEN)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(IdenticalConnectException.class)
	public ResponseEntity<ExceptionResonse> handleConnectException(
			IdenticalConnectException exception) {
		return ResponseEntity.status(BAD_REQUEST)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(ChatroomCountException.class)
	public ResponseEntity<ExceptionResonse> handleChatroomCountException(
			ChatroomCountException exception) {
		return ResponseEntity.status(BAD_REQUEST)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(ChatroomDuplicateException.class)
	public ResponseEntity<ExceptionResonse> handleChatroomDuplicateException(
			ChatroomDuplicateException exception) {
		return ResponseEntity.status(CONFLICT)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(ChatroomException.class)
	public ResponseEntity<ExceptionResonse> handleChatroomException(ChatroomException exception) {
		return ResponseEntity.status(BAD_REQUEST)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(ChatroomNotFoundException.class)
	public ResponseEntity<ExceptionResonse> handleChatroomNotFoundException(
			ChatroomNotFoundException exception) {
		return ResponseEntity.status(BAD_REQUEST)
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionResonse> handleRuntimeException(RuntimeException exception) {
		return ResponseEntity.status(FORBIDDEN.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(S3FileNotFoundException.class)
	public ResponseEntity<ExceptionResonse> handleS3FileNotFoundException(
			S3FileNotFoundException exception) {
		return ResponseEntity.status(NOT_FOUND.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}

	@ExceptionHandler(S3FileNameInvalidException.class)
	public ResponseEntity<ExceptionResonse> handleS3FileNameInvalidException(
			S3FileNameInvalidException exception) {
		return ResponseEntity.status(BAD_REQUEST.value())
				.body(new ExceptionResonse(false, exception.getMessage()));
	}
}
