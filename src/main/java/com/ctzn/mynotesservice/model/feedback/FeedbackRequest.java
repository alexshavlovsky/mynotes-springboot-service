package com.ctzn.mynotesservice.model.feedback;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {
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
}
