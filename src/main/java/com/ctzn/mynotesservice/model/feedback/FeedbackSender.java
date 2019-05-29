package com.ctzn.mynotesservice.model.feedback;

public interface FeedbackSender {
    void sendAsync(FeedbackRequest feedbackRequest);
}
