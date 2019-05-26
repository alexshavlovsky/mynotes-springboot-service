package com.ctzn.mynotesservice.components.email;

import com.ctzn.mynotesservice.model.feedback.FeedbackRequest;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class FeedbackSenderImplIntegrationTest {
    @Rule
    public GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String userPassword;

    @Autowired
    private FeedbackSender feedbackSender;

    @Value("${app.mail.feedback}")
    private String mailTo;

    @Test
    public void shouldSendMails() throws MessagingException {
        greenMail.setUser(userName, userPassword);

        int MAIL_COUNT = 3;
        ArrayList<FeedbackRequest> feedbackList = new ArrayList<>();
        for (int i = 0; i < MAIL_COUNT; i++) {
            FeedbackRequest feedbackRequest = new FeedbackRequest(
                    "user123@mail.com",
                    GreenMailUtil.random(10),
                    GreenMailUtil.random(20)
            );
            feedbackList.add(feedbackRequest);
            feedbackSender.sendAsync(feedbackRequest);
        }

        greenMail.waitForIncomingEmail(MAIL_COUNT);

        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertEquals(MAIL_COUNT, emails.length);
        for (int i = 0; i < MAIL_COUNT; i++) {
            assertEquals(mailTo, emails[i].getRecipients(Message.RecipientType.TO)[0].toString());
            assertEquals(feedbackList.get(i).getSenderEmail(), emails[i].getFrom()[0].toString());
            assertEquals("Feedback from " + feedbackList.get(i).getSenderName(), emails[i].getSubject());
            assertEquals(feedbackList.get(i).getFeedbackText(), GreenMailUtil.getBody(emails[i]));
        }
    }

}
