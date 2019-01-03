package com.everyone.crowd.entity.exception;

public class ErrorCodeException extends RuntimeException {
    private int code;

    public ErrorCodeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
