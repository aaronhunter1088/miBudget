package com.miBudget.utilities;

import com.miBudget.entities.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.util.Properties;
import java.util.logging.LogManager;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtility {
    private static Logger LOGGER = LoggerFactory.getLogger(EmailUtility.class);

    private final Properties properties;

    public EmailUtility() {
        this.properties = System.getProperties();
        setupProperties(this.properties);
    }

    private Properties setupProperties(Properties properties) {
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.enable", false);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.user", "aaronhunter@live.com");
        properties.put("mail.smtp.password", "****");
        properties.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return properties;
    }

    public void sendEmail(Email email) throws Exception {
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.smtp.user"), properties.getProperty("mail.smtp.password"));
            }
        });
        //Session session = Session.getInstance(properties, null);
        // Used to debug SMTP issues
        session.setDebug(true);
        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);
        // Set From: header field of the header.
        message.setFrom(new InternetAddress(email.getFrom()));
        // Set To: header field of the header.
        //message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
        // Set Subject: header field
        message.setSubject(email.getSubject());
        // Now set the actual message
        message.setText(email.getMessage());
        LOGGER.info("sending...");
        Transport.send(message);
        LOGGER.info("Sent message successfully....");
    }
}
