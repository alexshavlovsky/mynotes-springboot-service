package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.components.email.FeedbackSender;
import com.ctzn.mynotesservice.model.feedback.FeedbackRequest;
import com.ctzn.mynotesservice.model.feedback.FeedbackController;
import com.icegreen.greenmail.util.GreenMailUtil;
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
    FeedbackSender feedbackSender;

    private MockMvc mockMvc;

    private FeedbackRequest feedbackRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new FeedbackController(feedbackSender)).build();
        feedbackRequest = new FeedbackRequest(
                "user123@mail.com",
                GreenMailUtil.random(10),
                GreenMailUtil.random(50)
        );
    }

    @Test
    public void shouldPostFeedback() throws Exception {
        mockPostRequest(mockMvc, BASE_PATH, feedbackRequest, status().isAccepted(), null);

        ArgumentCaptor<FeedbackRequest> feedbackDTOCaptor = ArgumentCaptor.forClass(FeedbackRequest.class);
        verify(feedbackSender, times(1)).sendAsync(feedbackDTOCaptor.capture());
        verifyNoMoreInteractions(feedbackSender);
        Assert.assertEquals(feedbackRequest, feedbackDTOCaptor.getValue());
    }

    @Test
    public void shouldRejectInvalidFeedback() throws Exception {
        // invalid mail address
        feedbackRequest.setSenderEmail("user123_mail.com");

        mockPostRequest(mockMvc, BASE_PATH, feedbackRequest, status().isBadRequest(), null);

        verifyNoMoreInteractions(feedbackSender);
    }

}
