package com.easymed.utils;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.concurrent.Task;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Email class to send emails to the users using SMTP protocol.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class Email extends Task<Void> {

    /**
     * Recipient Email Address
     */
    private final String recipient;
    /**
     * Email subject
     */
    private final String subject;
    /**
     * Email content
     */
    private final String content;
    /**
     * Email username
     */
    private String username;
    /**
     * Email password
     */
    private String password;
    /**
     * Email from address
     */
    private String emailFromAddress;
    /**
     * Email from name
     */
    private String emailFromName;
    /**
     * Email properties to set SMTP configurations
     */
    private Properties properties;


    /**
     * Constructor to initialize the email properties and credentials from the environment variables.
     */
    public Email(String recipient, String subject, String content) {
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;

    }

    private void loadCredential() {
        Dotenv env = Dotenv.load();
        this.username = env.get("EMAIL_USERNAME");
        this.password = env.get("EMAIL_PASSWORD");
        this.emailFromAddress = env.get("EMAIL_FROM_ADDRESS");
        this.emailFromName = env.get("EMAIL_FROM_NAME");
        this.properties = new Properties();

        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.starttls.enable", "true");
        this.properties.put("mail.smtp.host", env.get("EMAIL_HOST"));
        this.properties.put("mail.smtp.port", env.get("EMAIL_PORT"));
    }

    public void send() throws MessagingException {
        this.loadCredential();

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = prepareMessage(session, this.recipient, this.subject, this.content);
        Transport.send(message);
    }

    private Message prepareMessage(Session session, String to, String subject, String content) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(this.emailFromAddress, this.emailFromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);
            message.setContent(content, "text/html");
        } catch (Exception e) {
            System.out.println("Error preparing the email message" + e.getMessage());
        }
        return message;
    }

    @Override
    protected Void call() throws Exception {
        send();
        return null;
    }
}