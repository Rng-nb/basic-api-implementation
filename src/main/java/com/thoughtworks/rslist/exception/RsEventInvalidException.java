package com.thoughtworks.rslist.exception;

import com.thoughtworks.rslist.api.RsController;

public class RsEventInvalidException extends RuntimeException{
    private String errorMessage;
    public RsEventInvalidException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
