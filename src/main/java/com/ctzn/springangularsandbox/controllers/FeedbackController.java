package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.components.email.FeedbackSender;
import com.ctzn.springangularsandbox.dto.FeedbackDTO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/feedback")
public class FeedbackController {
    private FeedbackSender feedbackSender;

    public FeedbackController(FeedbackSender feedbackSender) {
        this.feedbackSender = feedbackSender;
    }

    @PostMapping
    public void sendFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult.toString());
        else
            feedbackSender.send(feedbackDTO);
    }
}
