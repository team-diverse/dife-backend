package com.dife.api.exception;

public class ConnectUnauthorizedException extends RuntimeException {
    public ConnectUnauthorizedException() {
        super("접근 권한이 없는 커넥트입니다.");
    }
}
