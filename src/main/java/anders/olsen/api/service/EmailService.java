package anders.olsen.api.service;

import anders.olsen.api.entity.User;
import org.springframework.mail.SimpleMailMessage;

/**
 * Interface for mail sending.
 */
public interface EmailService {

    void sendEmail(SimpleMailMessage email);

    void sendVerifyEmail(User user);

}
