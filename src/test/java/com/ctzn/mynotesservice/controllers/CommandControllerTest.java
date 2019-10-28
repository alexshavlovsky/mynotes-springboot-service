package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.command.CommandController;
import com.ctzn.mynotesservice.model.command.CommandRequest;
import com.ctzn.mynotesservice.model.command.ShutdownManager;
import com.ctzn.mynotesservice.model.command.context.ExecutionContext;
import com.ctzn.mynotesservice.repositories.DbSeeder;
import com.ctzn.mynotesservice.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.ctzn.mynotesservice.controllers.RestTestUtil.mockPostRequest;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class CommandControllerTest {

    private static final String BASE_PATH = CommandController.BASE_PATH;

    @Mock
    private UserService userService;

    @Mock
    private DbSeeder dbSeeder;

    @Mock
    private ShutdownManager shutdownManager;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CommandController(userService, new ExecutionContext(dbSeeder, shutdownManager)))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        TimeSource.setFixed(true);
    }

    @After
    public void checkMocks() {
        verifyNoMoreInteractions(dbSeeder, shutdownManager);
    }

    @Test
    public void shouldAcceptAndExecuteShutdownCommand() throws Exception {
        CommandRequest command = new CommandRequest("shutdown");
        ApiMessage expected = new ApiMessage("Shutdown command accepted");
        mockPostRequest(mockMvc, BASE_PATH, command, status().isAccepted(), expected);
        verify(shutdownManager, times(1)).initiateShutdownAsync(0);
    }

    @Test
    public void shouldAcceptAndExecuteFillDatabaseCommand() throws Exception {
        CommandRequest command = new CommandRequest("fill database");
        ApiMessage expected = new ApiMessage("Fill database command accepted");
        mockPostRequest(mockMvc, BASE_PATH, command, status().isAccepted(), expected);
        verify(dbSeeder, times(1)).seedAsync();
    }

    @Test
    public void shouldRejectUnknownCommand() throws Exception {
        CommandRequest command = new CommandRequest("smile");
        ApiMessage expected = new ApiMessage("Command 'smile' not found");
        mockPostRequest(mockMvc, BASE_PATH, command, status().isNotFound(), expected);
    }

}
