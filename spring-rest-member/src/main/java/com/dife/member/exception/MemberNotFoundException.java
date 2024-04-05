package com.dife.member.exception;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(String message)
    {
        super(message);
    }

    public MemberNotFoundException(Throwable cause)
    {
        super(cause);
    }

    public MemberNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
