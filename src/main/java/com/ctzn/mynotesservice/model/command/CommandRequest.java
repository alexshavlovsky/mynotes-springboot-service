package com.ctzn.mynotesservice.model.command;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CommandRequest {
    @NotBlank
    @NonNull
    private String command;
}
