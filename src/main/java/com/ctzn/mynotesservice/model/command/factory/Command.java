package com.ctzn.mynotesservice.model.command.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Command {
    private String debugMessage;
    private CommandExecutor executor;
}
