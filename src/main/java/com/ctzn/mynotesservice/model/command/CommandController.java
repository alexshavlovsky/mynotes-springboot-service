package com.ctzn.mynotesservice.model.command;

import com.ctzn.mynotesservice.components.DbSeeder;
import com.ctzn.mynotesservice.components.ShutdownManager;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.exceptions.EntityByNameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// TODO: write tests for this controller
@RestController
@CrossOrigin
@RequestMapping(path = CommandController.BASE_PATH)
public class CommandController {

    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

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
    public ApiMessage executeCommand(@RequestBody CommandRequest commandRequest) throws EntityByNameNotFoundException {
        switch (commandRequest.getCommand()) {
            case "shutdown":
                logger.debug(SHUTDOWN_MSG);
                shutdownManager.initiateShutdown(0);
                return new ApiMessage(SHUTDOWN_MSG);
            case "fill database":
                logger.debug(FILL_DB_MSG);
                dbSeeder.run("force");
                return new ApiMessage(FILL_DB_MSG);
        }
        throw new EntityByNameNotFoundException("Command", commandRequest.getCommand());
    }

}