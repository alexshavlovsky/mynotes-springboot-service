package com.ctzn.mynotesservice.model.feedback;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FeedbackRequest {
    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    @NonNull
    private String senderEmail;

    @NotBlank
    @Size(max = 50)
    @NonNull
    private String senderName;

    @NotBlank
    @Size(max = 500)
    @NonNull
    private String feedbackText;
}
