package com.dife.member.exception;


public class UnAuthorizationException extends MemberException {

    public UnAuthorizationException(String message)
    {
        super(message);
    }

    public UnAuthorizationException(Throwable cause)
    {
        super(cause);
    }

    public UnAuthorizationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
