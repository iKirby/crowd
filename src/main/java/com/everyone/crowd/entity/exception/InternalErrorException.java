package com.everyone.crowd.entity.exception;

public class InternalErrorException extends ErrorCodeException {

    public InternalErrorException(String message) {
        super(500, message);
    }
}
