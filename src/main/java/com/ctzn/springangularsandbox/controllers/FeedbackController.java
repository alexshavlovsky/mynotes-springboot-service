package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.components.email.FeedbackSender;
import com.ctzn.springangularsandbox.dto.FeedbackDTO;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}

@RestController
@CrossOrigin
@RequestMapping(path = "/api/feedback")
public class FeedbackController {
    private FeedbackSender feedbackSender;
    private Logger logger;

    public FeedbackController(FeedbackSender feedbackSender, Logger logger) {
        this.feedbackSender = feedbackSender;
        this.logger = logger;
    }

    @PostMapping
    public void sendFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().toString();
            logger.error(errors);
            throw new BadRequest(errors);
        }
        logger.info("Sending feedback: " + feedbackDTO);
        feedbackSender.send(feedbackDTO);
    }
}
