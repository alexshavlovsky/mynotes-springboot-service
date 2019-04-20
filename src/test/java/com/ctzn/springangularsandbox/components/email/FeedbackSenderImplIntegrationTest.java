package com.ctzn.springangularsandbox.components.email;

import com.ctzn.springangularsandbox.dto.FeedbackDTO;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Before;
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

    @Before
    public void setUp() {
        greenMail.setUser(userName, userPassword);
    }

    @Autowired
    private FeedbackSender feedbackSender;

    @Value("${app.mail.feedback}")
    private String mailTo;

    @Test
    public void shouldSendSingleMail() throws MessagingException {
        FeedbackDTO feedbackDTO = new FeedbackDTO(
                "user123@mail.com",
                GreenMailUtil.random(10),
                GreenMailUtil.random(50)
        );

        feedbackSender.send(feedbackDTO);

        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertEquals(1, emails.length);
        assertEquals(mailTo, emails[0].getRecipients(Message.RecipientType.TO)[0].toString());
        assertEquals(feedbackDTO.getSenderEmail(), emails[0].getFrom()[0].toString());
        assertEquals("Feedback from " + feedbackDTO.getSenderName(), emails[0].getSubject());
        assertEquals(feedbackDTO.getFeedbackText(), GreenMailUtil.getBody(emails[0]));
    }

}
