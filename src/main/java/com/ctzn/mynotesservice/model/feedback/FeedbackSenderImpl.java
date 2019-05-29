package com.ctzn.mynotesservice.model.feedback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedbackSenderImpl implements FeedbackSender {

    private JavaMailSender mailSender;

    @Value("${app.mail.feedback}")
    private String mailTo;

    public FeedbackSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendAsync(FeedbackRequest feedbackRequest) {
        log.debug("Sending {}", feedbackRequest);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailTo);
        message.setSubject("Feedback from " + feedbackRequest.getSenderName());
        message.setText(feedbackRequest.getFeedbackText());
        message.setFrom(feedbackRequest.getSenderEmail());
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("{} {}", e.getMessage(), feedbackRequest);
            return;
        }
        log.debug("Sent {}", feedbackRequest);
    }
}
