package com.dife.member.exception;

public class DuplicateException extends MemberException{

    public DuplicateException(String message)
    {
        super(message);
    }

    public DuplicateException(Throwable cause)
    {
        super(cause);
    }

    public DuplicateException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
