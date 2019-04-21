package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.components.email.FeedbackSender;
import com.ctzn.springangularsandbox.dto.FeedbackDTO;
import com.icegreen.greenmail.util.GreenMailUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.ctzn.springangularsandbox.controllers.RestTestUtil.mockPostRequest;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class FeedbackControllerTest {

    private static final String BASE_PATH = FeedbackController.BASE_PATH;

    @Mock
    FeedbackSender feedbackSender;

    @Mock
    Logger logger;

    private MockMvc mockMvc;

    private FeedbackDTO feedbackDTO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new FeedbackController(feedbackSender, logger)).build();
        feedbackDTO = new FeedbackDTO(
                "user123@mail.com",
                GreenMailUtil.random(10),
                GreenMailUtil.random(50)
        );
    }

    @Test
    public void shouldPostFeedback() throws Exception {
        mockPostRequest(mockMvc, BASE_PATH, feedbackDTO, status().isAccepted(), null);

        ArgumentCaptor<FeedbackDTO> feedbackDTOCaptor = ArgumentCaptor.forClass(FeedbackDTO.class);
        verify(feedbackSender, times(1)).send(feedbackDTOCaptor.capture());
        verifyNoMoreInteractions(feedbackSender);
        Assert.assertEquals(feedbackDTO, feedbackDTOCaptor.getValue());
    }

    @Test
    public void shouldRejectInvalidFeedback() throws Exception {
        // invalid mail address
        feedbackDTO.setSenderEmail("user123_mail.com");

        mockPostRequest(mockMvc, BASE_PATH, feedbackDTO, status().isBadRequest(), null);

        verifyNoMoreInteractions(feedbackSender);
    }

}
