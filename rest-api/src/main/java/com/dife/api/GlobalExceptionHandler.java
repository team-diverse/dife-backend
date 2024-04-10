package com.dife.api;

import com.dife.api.exception.DuplicateException;
import com.dife.api.exception.MemberException;
import com.dife.api.exception.RegisterException;
import com.dife.api.exception.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ExceptionResonse> handleMemberException(MemberException exception)
    {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ExceptionResonse> handleDuplicateException(DuplicateException exception)
    {
        return ResponseEntity
                .status(CONFLICT.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResonse> handleException(Exception exception)
    {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<ExceptionResonse> handleRegisterException(RegisterException exception)
    {
        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResonse> handleIllegalArgumentException(IllegalArgumentException exception)
    {
        return ResponseEntity
                .status(UNAUTHORIZED.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ExceptionResonse> handlePostNotFoundException(PostNotFoundException exception)
    {
        return ResponseEntity
                .status(UNAUTHORIZED.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }


}
