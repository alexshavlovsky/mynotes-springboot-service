package com.ctzn.mynotesservice.model.apimessage;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception {
    private final HttpStatus httpStatus;

    HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public static final ApiException INVALID_EMAIL_PASSWORD = new ApiException("Invalid email/password supplied", HttpStatus.NOT_FOUND);

    public static ApiException getAlreadyInUse(String className, String entityName) {
        return new ApiException(className + " '" + entityName + "' already taken", HttpStatus.CONFLICT);
    }

    public static ApiException getCredentialsNotExist() {
        return new ApiException("Credentials not exist", HttpStatus.FORBIDDEN);
    }

    public static ApiException getBadRequest(String message) {
        return new ApiException(message, HttpStatus.BAD_REQUEST);
    }

    public static ApiException getNotFoundById(String className, long entityId) {
        return new ApiException(className + " with id=" + entityId + " not found", HttpStatus.NOT_FOUND);
    }

    public static ApiException getNotFoundByName(String className, String entityName) {
        return new ApiException(className + " '" + entityName + "' not found", HttpStatus.NOT_FOUND);
    }

}
