package com.ctzn.springangularsandbox.components.email;

import com.ctzn.springangularsandbox.dto.FeedbackDTO;
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
    public void sendAsync(FeedbackDTO feedbackDTO) {
        logger.debug("Sending {}", feedbackDTO);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailTo);
        message.setSubject("Feedback from " + feedbackDTO.getSenderName());
        message.setText(feedbackDTO.getFeedbackText());
        message.setFrom(feedbackDTO.getSenderEmail());
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("{} {}", e.getMessage(), feedbackDTO);
            return;
        }
        logger.debug("Sent {}", feedbackDTO);
    }
}
