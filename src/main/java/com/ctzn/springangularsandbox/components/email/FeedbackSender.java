package com.ctzn.springangularsandbox.components.email;

public interface FeedbackSender {
    void send(String from, String name, String text);
}
