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

    private static final ApiException INVALID_EMAIL_PASSWORD = new ApiException("Invalid email/password supplied", HttpStatus.NOT_FOUND);
    private static final ApiException ACCESS_DENIED = new ApiException("Access denied", HttpStatus.FORBIDDEN);

    public static ApiException getAlreadyInUse(String className, String entityName) {
        return new ApiException(className + " '" + entityName + "' already taken", HttpStatus.CONFLICT);
    }

    public static ApiException getInvalidEmailPassword() {
        return INVALID_EMAIL_PASSWORD;
    }

    public static ApiException getAccessDenied() {
        return ACCESS_DENIED;
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
