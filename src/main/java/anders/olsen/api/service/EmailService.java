package anders.olsen.api.service;

import org.springframework.mail.SimpleMailMessage;

/**
 * Interface for mail sending.
 */
public interface EmailService {

    public void sendEmail(SimpleMailMessage email);
}
