package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.components.email.FeedbackSender;
import com.ctzn.springangularsandbox.dto.FeedbackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.ctzn.springangularsandbox.Util.customMessage;

@RestController
@CrossOrigin
@RequestMapping(path = FeedbackController.BASE_PATH)
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    static final String BASE_PATH = "/api/feedback";

    private FeedbackSender feedbackSender;

    public FeedbackController(FeedbackSender feedbackSender) {
        this.feedbackSender = feedbackSender;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map sendFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) {
        feedbackSender.sendAsync(feedbackDTO);
        logger.debug("Accepted {}", feedbackDTO);
        return customMessage("Feedback accepted");
    }

}
