package com.dife.api.exception;


public class MemberUnAuthorizationException extends UnAuthorizationException {

    public MemberUnAuthorizationException(String message)
    {
        super(message);
    }

    public MemberUnAuthorizationException(Throwable cause)
    {
        super(cause);
    }

    public MemberUnAuthorizationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
