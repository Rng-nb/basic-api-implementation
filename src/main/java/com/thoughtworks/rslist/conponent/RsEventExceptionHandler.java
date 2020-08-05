package com.thoughtworks.rslist.conponent;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventInvalidIndexException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {
    @ExceptionHandler({RsEventInvalidIndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity RsEventExceptionHandler(Exception e) {
        String errorMessage;
        if(e instanceof RsEventInvalidIndexException) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = "invalid param";
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
