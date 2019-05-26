package com.ctzn.mynotesservice.model.feedback;

import com.ctzn.mynotesservice.components.email.FeedbackSender;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(path = FeedbackController.BASE_PATH)
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    public static final String BASE_PATH = "/api/feedback";

    private FeedbackSender feedbackSender;

    public FeedbackController(FeedbackSender feedbackSender) {
        this.feedbackSender = feedbackSender;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    // TODO: implement validation
    public ApiMessage sendFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        feedbackSender.sendAsync(feedbackRequest);
        logger.debug("Accepted {}", feedbackRequest);
        return new ApiMessage("Feedback accepted");
    }

}
