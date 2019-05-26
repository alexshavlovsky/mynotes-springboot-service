package com.ctzn.mynotesservice.model.apimessage.exceptions;

import org.springframework.http.HttpStatus;

public class EntityByNameNotFoundException extends Exception implements HttpStatusContainer {

    public EntityByNameNotFoundException(String className, String entityName) {
        super(className + " '" + entityName + "' not found");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
