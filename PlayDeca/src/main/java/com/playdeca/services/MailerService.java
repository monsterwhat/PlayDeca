package com.playdeca.services;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 *
 * @author Al
 */
@Named
@ApplicationScoped
public class MailerService {
    
    @Inject
    Mailer mailer;
    
    public void sendEmail(String to, String subject, String body) {
        try {
            mailer.send(
                Mail.withText(to, subject, body)
            );
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}