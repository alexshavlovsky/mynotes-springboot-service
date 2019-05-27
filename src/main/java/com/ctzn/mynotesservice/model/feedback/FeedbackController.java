package com.ctzn.mynotesservice.model.feedback;

import com.ctzn.mynotesservice.components.email.FeedbackSender;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.BindingResultAdapter;
import com.ctzn.mynotesservice.model.apimessage.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(path = FeedbackController.BASE_PATH)
@Slf4j
public class FeedbackController {

    public static final String BASE_PATH = "/api/feedback";

    private FeedbackSender feedbackSender;

    public FeedbackController(FeedbackSender feedbackSender) {
        this.feedbackSender = feedbackSender;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    // TODO: implement validation
    public ApiMessage sendFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest, BindingResult result) throws BadRequestException {
        if (result.hasErrors()) throw new BadRequestException(BindingResultAdapter.adapt(result));
        feedbackSender.sendAsync(feedbackRequest);
        log.debug("Accepted ", feedbackRequest);
        return new ApiMessage("Feedback accepted");
    }

}
