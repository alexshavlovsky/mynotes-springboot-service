package com.ctzn.mynotesservice.model.command;

import com.ctzn.mynotesservice.repositories.DbSeeder;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// TODO: write tests for this controller
@RestController
@CrossOrigin
@RequestMapping(path = CommandController.BASE_PATH)
@Slf4j
public class CommandController {

    public static final String BASE_PATH = "/api/command";
    private static final String SHUTDOWN_MSG = "Shutdown command accepted";
    private static final String FILL_DB_MSG = "Fill database accepted";

    private DbSeeder dbSeeder;
    private ShutdownManager shutdownManager;

    public CommandController(DbSeeder dbSeeder, ShutdownManager shutdownManager) {
        this.dbSeeder = dbSeeder;
        this.shutdownManager = shutdownManager;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiMessage executeCommand(@RequestBody CommandRequest commandRequest) throws ApiException {
        switch (commandRequest.getCommand()) {
            case "shutdown":
                log.debug(SHUTDOWN_MSG);
                shutdownManager.initiateShutdown(0);
                return new ApiMessage(SHUTDOWN_MSG);
            case "fill database":
                log.debug(FILL_DB_MSG);
                dbSeeder.run("force");
                return new ApiMessage(FILL_DB_MSG);
        }
        throw ApiException.getNotFoundByName("Command", commandRequest.getCommand());
    }

}
