package com.dife.api;

import com.dife.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResonse> handleMemberException(MemberException exception)
    {
        return ResponseEntity
                .status(UNAUTHORIZED.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException exception)
    {
        return ResponseEntity
                .status(BAD_REQUEST.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ExceptionResonse> handleDuplicateException(DuplicateException exception)
    {
        return ResponseEntity
                .status(CONFLICT.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResonse> handleNullPointerException(Exception exception)
    {
        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResonse> handleException(Exception exception)
    {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.value())
                .body(new ExceptionResonse(exception.getMessage()));
    }


}
