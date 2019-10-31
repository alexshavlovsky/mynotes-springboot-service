package com.ctzn.mynotesservice.model.command;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.command.context.ExecutionContext;
import com.ctzn.mynotesservice.model.user.UserRole;
import com.ctzn.mynotesservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = CommandController.BASE_PATH)
@Slf4j
public class CommandController {

    public static final String BASE_PATH = "/api/command";

    private UserService userService;
    private ExecutionContext executionContext;

    public CommandController(UserService userService, ExecutionContext executionContext) {
        this.userService = userService;
        this.executionContext = executionContext;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiMessage executeCommand(@RequestBody CommandRequest commandRequest, Authentication auth) throws ApiException {
        userService.getUserAssertRole(auth, UserRole.ADMIN);
        String keyWord = commandRequest.getCommand();
        if (!executionContext.commandWithKeyWordExists(keyWord)) {
            log.debug("Unknown command received: '{}'", keyWord);
            throw ApiException.getNotFoundByName("Command", commandRequest.getCommand());
        }
        String msg = executionContext.executeCommandByKeyWord(keyWord);
        log.debug(msg);
        return new ApiMessage(msg);
    }

}
