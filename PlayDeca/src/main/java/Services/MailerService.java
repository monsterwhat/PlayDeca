package Services;

import jakarta.annotation.Resource;
import jakarta.inject.Named;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.io.Serializable;

/**
 *
 * @author Al
 */

@Named
@Transactional
public class MailerService implements Serializable {
    
    @Resource(name = "mail/Playdeca") private Session mailSession;
    
    public void sendEmail(String to, String subject, String body) {
        try {
            // Create a new MimeMessage
            MimeMessage message = new MimeMessage(mailSession);

            // Set the recipient, subject, and content
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the message using the Transport class
            Transport.send(message);
        } catch (MessagingException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
    
}
