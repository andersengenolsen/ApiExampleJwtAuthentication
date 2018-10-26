package anders.olsen.api.controller;

import anders.olsen.api.entity.User;
import anders.olsen.api.entity.VerifyToken;
import anders.olsen.api.repository.VerifyEmailTokenRepository;
import anders.olsen.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

/**
 * Helper class for sending emails
 */
public class EmailSender {

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerifyEmailTokenRepository verifyRepo;
    
    /**
     * Helper method for sending a verification email to the user.
     * <p>
     * Generating token for validating email, send link to verify.
     *
     * @param user newly registered user.
     */
    public void sendVerifyEmail(User user) {

        final String url = "localhost:5000/api/auth/verify/";

        VerifyToken verifyToken = new VerifyToken(UUID.randomUUID().toString(), user);

        verifyRepo.save(verifyToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("support@olsenapi.com");
        message.setTo(user.getEmail());
        message.setSubject("Verify email");
        message.setText("Press this link to verify your email: " + url + verifyToken.getToken());

        emailService.sendEmail(message);
    }
}
