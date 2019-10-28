package com.ctzn.mynotesservice.model.feedback;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.BindingResultAdapter;
import com.ctzn.mynotesservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping(path = FeedbackController.BASE_PATH)
@Slf4j
public class FeedbackController {

    public static final String BASE_PATH = "/api/feedback";

    private UserService userService;
    private FeedbackSender feedbackSender;

    public FeedbackController(UserService userService, FeedbackSender feedbackSender) {
        this.userService = userService;
        this.feedbackSender = feedbackSender;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiMessage sendFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest,
                                   BindingResult result, Principal principal) throws ApiException {
        userService.getUser(principal); // check if the user is not removed or blocked
        if (result.hasErrors()) throw ApiException.getBadRequest(BindingResultAdapter.adapt(result));
        feedbackSender.sendAsync(feedbackRequest);
        log.debug("Accepted {}", feedbackRequest);
        return new ApiMessage("Feedback accepted");
    }

}
