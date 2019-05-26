package com.ctzn.springangularsandbox.model.apimessage.exceptions;

import org.springframework.http.HttpStatus;

public class EntityByIdNotFoundException extends Exception implements HttpStatusContainer {

    public EntityByIdNotFoundException(String className, long entityId) {
        super(className + " with id=" + entityId + " not found");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
