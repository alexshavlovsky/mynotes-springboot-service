package com.ctzn.mynotesservice.model.apimessage.exceptions;

import org.springframework.http.HttpStatus;

public interface HttpStatusContainer {
    HttpStatus getHttpStatus();
}
