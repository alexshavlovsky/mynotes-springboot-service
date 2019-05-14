package com.ctzn.springangularsandbox.dto;

import javax.validation.constraints.NotBlank;

public class CommandDto {

    @NotBlank
    private String command;

    public CommandDto() {
    }

    public CommandDto(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
