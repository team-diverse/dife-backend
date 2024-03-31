package com.dife.member.exception;

public class ForbiddenException extends MemberException{

    public ForbiddenException(String message)
    {
        super(message);
    }

    public ForbiddenException(Throwable cause)
    {
        super(cause);
    }

    public ForbiddenException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
