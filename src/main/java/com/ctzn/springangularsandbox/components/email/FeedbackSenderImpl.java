package com.ctzn.springangularsandbox.components.email;

import com.ctzn.springangularsandbox.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class FeedbackSenderImpl implements FeedbackSender {
    private JavaMailSender mailSender;

    @Value("${app.mail.feedback}")
    private String mailTo;

    public FeedbackSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(FeedbackDTO feedbackDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailTo);
        message.setSubject("Feedback from " + feedbackDTO.getSenderName());
        message.setText(feedbackDTO.getFeedbackText());
        message.setFrom(feedbackDTO.getSenderEmail());
        mailSender.send(message);
    }
}
