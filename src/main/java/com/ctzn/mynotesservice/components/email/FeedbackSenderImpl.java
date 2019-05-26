package com.ctzn.mynotesservice.components.email;

import com.ctzn.mynotesservice.model.feedback.FeedbackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class FeedbackSenderImpl implements FeedbackSender {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackSenderImpl.class);

    private JavaMailSender mailSender;

    @Value("${app.mail.feedback}")
    private String mailTo;

    public FeedbackSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendAsync(FeedbackRequest feedbackRequest) {
        logger.debug("Sending {}", feedbackRequest);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailTo);
        message.setSubject("Feedback from " + feedbackRequest.getSenderName());
        message.setText(feedbackRequest.getFeedbackText());
        message.setFrom(feedbackRequest.getSenderEmail());
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("{} {}", e.getMessage(), feedbackRequest);
            return;
        }
        logger.debug("Sent {}", feedbackRequest);
    }
}
