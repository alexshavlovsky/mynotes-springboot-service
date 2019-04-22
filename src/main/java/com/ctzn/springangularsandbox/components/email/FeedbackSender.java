package com.ctzn.springangularsandbox.components.email;

import com.ctzn.springangularsandbox.dto.FeedbackDTO;

public interface FeedbackSender {
    void sendAsync(FeedbackDTO feedbackDTO);
}
