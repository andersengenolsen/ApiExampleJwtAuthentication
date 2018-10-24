package anders.olsen.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link EmailService}.
 * Used for sending forgot password emails.
 */
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    /**
     * Properties for this bean must be set in ApplicationProperties.
     * Excluded for obvious reasons..
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Sending email message.
     *
     * @param email content
     */
    @Async
    @Override
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}
