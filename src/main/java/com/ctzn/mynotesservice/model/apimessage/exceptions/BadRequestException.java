package com.ctzn.mynotesservice.model.apimessage.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends Exception implements HttpStatusContainer {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
