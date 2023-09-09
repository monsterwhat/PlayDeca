package Utils;

/**
 *
 * @author Al
 */

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtils {
    
    public void sendEmail(String to, String subject, String body) {
        try {
        // Define properties for the mail session
        Properties props = new Properties();
        props.put("mail.smtp.host", "playdeca.com"); // Replace with your SMTP server address
        props.put("mail.smtp.port", "25"); // Replace with your SMTP server port
        props.put("mail.smtp.auth", "true"); // Enable authentication

        // Create an authenticator to provide credentials
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply", "noreply");
            }
        };

        // Create a mail session with authentication
        Session session = Session.getInstance(props, auth);

            // Create a new MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Set the recipient, subject, and content
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the message using the Transport class
            Transport.send(message);
        } catch (Exception e) {
            System.out.println("Error mailing: " + e.getLocalizedMessage());
        }
    }
}

