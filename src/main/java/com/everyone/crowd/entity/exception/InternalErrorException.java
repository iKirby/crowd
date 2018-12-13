package com.everyone.crowd.entity.exception;

public class InternalErrorException extends ErrorCodeException {

    public InternalErrorException(int errorCode, String message) {
        super(500, message);
    }
}
