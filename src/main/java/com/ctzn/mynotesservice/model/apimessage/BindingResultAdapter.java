package com.ctzn.mynotesservice.model.apimessage;

import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class BindingResultAdapter {
    public static String adapt(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(f -> f.getField() + ":" + f.getDefaultMessage())
                .collect(Collectors.joining(";"));
    }
}
