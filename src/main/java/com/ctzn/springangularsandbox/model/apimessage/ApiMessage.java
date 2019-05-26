package com.ctzn.springangularsandbox.model.apimessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ApiMessage {
    private final Date timestamp = new Date();
    private String message;
}
