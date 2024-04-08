package com.dife.api.exception;

public class MemberNotFoundException extends MemberException {

    public MemberNotFoundException() {
        super("해당 멤버는 존재하지 않습니다.");
    }

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
