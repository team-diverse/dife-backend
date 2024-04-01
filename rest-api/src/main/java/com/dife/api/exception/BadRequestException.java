package com.dife.api.exception;

public class BadRequestException extends MemberException{

    public BadRequestException(String message)
    {
        super(message);
    }

    public BadRequestException(Throwable cause)
    {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
