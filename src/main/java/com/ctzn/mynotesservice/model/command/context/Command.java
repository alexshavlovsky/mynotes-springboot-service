package com.ctzn.mynotesservice.model.command.context;

class Command {
    private String debugMessage;
    private IStrategy commandStrategy;

    Command(String debugMessage, IStrategy commandStrategy) {
        this.debugMessage = debugMessage;
        this.commandStrategy = commandStrategy;
    }

    String getDebugMessage() {
        return debugMessage;
    }

    void apply() {
        commandStrategy.execute();
    }
}
