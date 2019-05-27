package com.ctzn.mynotesservice.model.apimessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ApiMessage {
    private final Date timestamp = TimestampSource.getTimestamp();
    private String message;
}
