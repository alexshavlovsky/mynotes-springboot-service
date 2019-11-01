package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.feedback.FeedbackController;
import com.ctzn.mynotesservice.model.feedback.FeedbackRequest;
import com.ctzn.mynotesservice.model.feedback.FeedbackSender;
import com.ctzn.mynotesservice.model.user.UserRole;
import com.ctzn.mynotesservice.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.ctzn.mynotesservice.controllers.RestTestUtil.mockPostRequest;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class FeedbackControllerTest {

    private static final String BASE_PATH = FeedbackController.BASE_PATH;

    @Mock
    private UserService userService;

    @Mock
    FeedbackSender feedbackSender;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FeedbackController(userService, feedbackSender))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        TimeSource.setFixed(true);
    }

    @After
    public void checkMocks() {
        verifyNoMoreInteractions(feedbackSender, userService);
    }

    @Test
    public void shouldPostFeedbacks() throws Exception {
        FeedbackRequest feedback1 = new FeedbackRequest(
                "user123@mail.com",
                "Alex",
                "Feedback 1"
        );

        FeedbackRequest feedback2 = new FeedbackRequest(
                "tom@mail.com",
                "Tom",
                "Feedback 2"
        );

        ApiMessage expected = new ApiMessage("Feedback accepted");
        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(null);

        mockPostRequest(mockMvc, BASE_PATH, feedback1, status().isAccepted(), expected);
        mockPostRequest(mockMvc, BASE_PATH, feedback2, status().isAccepted(), expected);

        verify(userService, times(2)).getUserAssertRole(any(), eq(UserRole.USER));
        ArgumentCaptor<FeedbackRequest> captor = ArgumentCaptor.forClass(FeedbackRequest.class);
        verify(feedbackSender, times(2)).sendAsync(captor.capture());
        Assert.assertEquals(feedback1, captor.getAllValues().get(0));
        Assert.assertEquals(feedback2, captor.getAllValues().get(1));
    }

    @Test
    public void shouldRejectInvalidFeedbacks() throws Exception {
        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(null);

        // invalid mail address
        mockPostRequest(mockMvc, BASE_PATH,
                new FeedbackRequest("user123_mail.com", "Alex", "Feedback text"),
                status().isBadRequest(), null);

        // blank user name
        mockPostRequest(mockMvc, BASE_PATH,
                new FeedbackRequest("user123@mail.com", " ", "Feedback text"),
                status().isBadRequest(), null);

        // blank feedback text
        mockPostRequest(mockMvc, BASE_PATH,
                new FeedbackRequest("user123@mail.com", "Alex", " "),
                status().isBadRequest(), null);

        verify(userService, times(3)).getUserAssertRole(any(), eq(UserRole.USER));
    }

}
