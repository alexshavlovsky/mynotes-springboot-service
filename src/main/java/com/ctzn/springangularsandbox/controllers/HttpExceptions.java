package com.ctzn.springangularsandbox.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class HttpExceptions {
    static final NotFound ENTITY_NOT_FOUND = new NotFound("Entity not found");
    static final BadRequest ENTITY_ID_SHOULD_BE_NULL = new BadRequest("Entity id should be null");
    static final BadRequest ENTITY_ID_IS_WRONG = new BadRequest("Entity id is wrong");

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    static class BadRequest extends RuntimeException {
        BadRequest(String message) {
            super(message);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static class NotFound extends RuntimeException {
        NotFound(String message) {
            super(message);
        }
    }
}
