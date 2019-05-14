package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.components.DbSeeder;
import com.ctzn.springangularsandbox.components.ShutdownManager;
import com.ctzn.springangularsandbox.dto.CommandDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.ctzn.springangularsandbox.Util.customMessage;
import static com.ctzn.springangularsandbox.controllers.RequestValidator.validateObjectExists;

// TODO: write tests this controller
@RestController
@CrossOrigin
@RequestMapping(path = CommandController.BASE_PATH)
public class CommandController {

    private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

    static final String BASE_PATH = "/api/command";
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
    public Map command(@Valid @RequestBody CommandDto commandDto) {
        switch (commandDto.getCommand()) {
            case "shutdown":
                logger.debug(SHUTDOWN_MSG);
                shutdownManager.initiateShutdown(0);
                return customMessage(SHUTDOWN_MSG);
            case "fill database":
                logger.debug(FILL_DB_MSG);
                dbSeeder.run("force");
                return customMessage(FILL_DB_MSG);
        }
        // throw "Command not found"
        validateObjectExists(false, "Command");
        // unreachable
        throw new AssertionError();
    }

}
