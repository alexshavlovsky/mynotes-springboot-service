package com.ctzn.springangularsandbox.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class FeedbackDTO {
    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    private String senderEmail;

    @NotBlank
    @Size(max = 50)
    private String senderName;

    @NotBlank
    @Size(max = 500)
    private String feedbackText;

    public FeedbackDTO() {
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public FeedbackDTO(String senderEmail, String senderName, String feedbackText) {
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.feedbackText = feedbackText;
    }

    @Override
    public String toString() {
        return "FeedbackDTO{" +
                "senderEmail='" + senderEmail + '\'' +
                ", senderName='" + senderName + '\'' +
                ", feedbackText='" + feedbackText + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackDTO that = (FeedbackDTO) o;
        return senderEmail.equals(that.senderEmail) &&
                senderName.equals(that.senderName) &&
                feedbackText.equals(that.feedbackText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderEmail, senderName, feedbackText);
    }
}
