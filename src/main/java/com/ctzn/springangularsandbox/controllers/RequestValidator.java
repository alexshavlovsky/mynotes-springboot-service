package com.ctzn.springangularsandbox.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

class RequestValidator {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class BadRequest extends RuntimeException {
        BadRequest(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class NotFound extends RuntimeException {
        NotFound(String message) {
            super(message);
        }
    }

    static void validateObjectExists(Boolean objectExists, String objectName) {
        if (!objectExists) throw new NotFound(objectName + " not found");
    }

    static <T> T validateObjectExists(Optional<T> optional, String objectName) {
        return optional.orElseThrow(() -> new NotFound(objectName + " not found"));
    }

    static void validateIdIsNull(Long objectId, String objectName) {
        if (objectId != null) throw new BadRequest(objectName + " id must be null");
    }

    static void validateIdNotNull(Long objectId, String objectName) {
        if (objectId == null) throw new BadRequest(objectName + " id is required");
    }

    static void validateIdNotNullAndEqual(Long objectId, Long pathId, String objectName) {
        validateIdNotNull(objectId, objectName);
        if (!objectId.equals(pathId))
            throw new BadRequest(String.format("%s {id=%s} does not match path {id=%s}", objectName, objectId, pathId));
    }

}
