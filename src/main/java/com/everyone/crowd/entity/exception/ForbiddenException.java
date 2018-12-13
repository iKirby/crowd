package com.everyone.crowd.entity.exception;

public class ForbiddenException extends ErrorCodeException {

    public ForbiddenException(String message) {
        super(403, message);
    }
}
