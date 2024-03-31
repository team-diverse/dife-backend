package com.dife.member.exception;

public class DuplicateMemberException extends DuplicateException{

    public DuplicateMemberException(String message)
    {
        super(message);
    }

    public DuplicateMemberException(Throwable cause)
    {
        super(cause);
    }

    public DuplicateMemberException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
