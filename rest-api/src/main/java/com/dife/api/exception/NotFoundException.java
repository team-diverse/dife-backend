package com.dife.api.exception;


public class NotFoundException extends MemberException {

    public NotFoundException(String message)
    {
        super(message);
    }

    public NotFoundException(Throwable cause)
    {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
