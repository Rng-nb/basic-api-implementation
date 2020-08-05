package com.thoughtworks.rslist.exception;

public class RsEventInvalidIndexException extends RuntimeException{
    private String errorMessage;
    public RsEventInvalidIndexException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
