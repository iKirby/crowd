package com.everyone.crowd.entity.exception;

public class NotFoundException extends ErrorCodeException {

    public NotFoundException(String message) {
        super(404, message);
    }
}
