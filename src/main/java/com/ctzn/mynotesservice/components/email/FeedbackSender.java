package com.ctzn.mynotesservice.components.email;

import com.ctzn.mynotesservice.model.feedback.FeedbackRequest;

public interface FeedbackSender {
    void sendAsync(FeedbackRequest feedbackRequest);
}
