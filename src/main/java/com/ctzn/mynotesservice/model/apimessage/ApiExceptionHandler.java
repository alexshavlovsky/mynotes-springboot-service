package com.ctzn.mynotesservice.model.apimessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiMessage> handleApiExceptions(ApiException ex) {
        return new ResponseEntity<>(new ApiMessage(ex.getMessage()), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiMessage> handleOtherExceptions(Exception ex) {
        String message = ex.getMessage();
        if (message == null) message = "Unknown error";
        return new ResponseEntity<>(new ApiMessage(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
