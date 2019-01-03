package com.everyone.crowd.entity.exception;

public class NotAcceptableException extends ErrorCodeException {

    public NotAcceptableException(String message) {
        super(406, message);
    }
}
