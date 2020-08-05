package com.thoughtworks.rslist.conponent;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {
    @ExceptionHandler({RsEventInvalidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity RsEventExceptionHandler(Exception e) {
        String errorMessage = null;
        if(e instanceof RsEventInvalidException) {
            errorMessage = e.getMessage();
        } else if (((MethodArgumentNotValidException) e).getBindingResult().getTarget() instanceof RsEvent) {
                errorMessage = "invalid param";
        } else if(((MethodArgumentNotValidException) e).getBindingResult().getTarget() instanceof User){
            errorMessage = "invalid user";
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
