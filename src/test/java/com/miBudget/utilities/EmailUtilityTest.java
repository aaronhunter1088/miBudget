package com.miBudget.utilities;

import com.miBudget.entities.Email;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(JUnit4.class)
public class EmailUtilityTest {

    private EmailUtility emailUtility;

    public EmailUtilityTest() {
        emailUtility = new EmailUtility();
    }

    @Test
    public void emailUtilitySendsEmail() {
        String resetPasswordLink = "miBudget.com/password-reset";
        StringBuffer message = new StringBuffer();
        message.append("Hi there ").append("User").append(", ").append("\n");
        message.append("We have recovered your password: ").append("password").append("\n");
        message.append("Please try to log back in. If that still doesn't work, you can click the following link to reset your password: ").append("\n");
        message.append(resetPasswordLink);
        Email testMe = new Email("aaronhunter@live.com", "aaronhunter@live.com", "Test Password Recovery", message.toString());
        try {
            emailUtility.sendEmail(testMe);
        } catch (Exception messagingException) {
            messagingException.printStackTrace();
            fail();
        }
    }
}
