package com.dife.api.exception;

public class ConnectDuplicateException extends RuntimeException {
    public ConnectDuplicateException() {
        super("이미 유저 사이에 커넥트가 존재합니다.");
    }
}
