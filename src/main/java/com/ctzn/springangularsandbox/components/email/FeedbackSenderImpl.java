package com.ctzn.springangularsandbox.components.email;


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
    public void send(String from, String name, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailTo);
        message.setSubject("Feedback from " + name);
        message.setText(text);
        message.setFrom(from);
        mailSender.send(message);
    }
}
