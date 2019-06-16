package com.ctzn.mynotesservice.model.command;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.command.factory.Command;
import com.ctzn.mynotesservice.model.command.factory.CommandFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = CommandController.BASE_PATH)
@Slf4j
public class CommandController {

    public static final String BASE_PATH = "/api/command/";

    private CommandFactory commandFactory;

    public CommandController(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiMessage executeCommand(@RequestBody CommandRequest commandRequest) throws ApiException {
        String keyWord = commandRequest.getCommand();
        if (!commandFactory.commandWithKeyWordExists(keyWord)) {
            log.debug("Unknown command received: '{}'", keyWord);
            throw ApiException.getNotFoundByName("Command", commandRequest.getCommand());
        }
        Command command = commandFactory.getCommandByKeyWord(keyWord);
        log.debug(command.getDebugMessage());
        command.getExecutor().execute();
        return new ApiMessage(command.getDebugMessage());
    }

}
