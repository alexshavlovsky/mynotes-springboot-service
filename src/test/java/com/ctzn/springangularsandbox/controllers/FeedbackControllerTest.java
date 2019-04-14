package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.components.email.FeedbackSender;
import com.ctzn.springangularsandbox.dto.FeedbackDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMailUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class FeedbackControllerTest {

    FeedbackController feedbackController;

    @Mock
    FeedbackSender feedbackSender;

    @Mock
    Logger logger;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        feedbackController = new FeedbackController(feedbackSender, logger);
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackController).build();
    }

    @Test
    public void mustPostFeedback() throws Exception {
        FeedbackDTO feedbackDTO = new FeedbackDTO(
                "user123@mail.com",
                GreenMailUtil.random(10),
                GreenMailUtil.random(50)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/feedback")
                .content(asJsonString(feedbackDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
                );

        ArgumentCaptor<FeedbackDTO> feedbackDTOCaptor = ArgumentCaptor.forClass(FeedbackDTO.class);
        verify(feedbackSender, times(1)).send(feedbackDTOCaptor.capture());
        Assert.assertEquals(feedbackDTO, feedbackDTOCaptor.getValue());
    }

    @Test
    public void mustRejectInvalidFeedback() throws Exception {
        FeedbackDTO feedbackDTO = new FeedbackDTO(
                "user123_mail.com",
                GreenMailUtil.random(10),
                GreenMailUtil.random(50)
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/feedback")
                .content(asJsonString(feedbackDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
        ;

        verify(feedbackSender, times(0)).send(any());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
