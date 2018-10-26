package anders.olsen.api.service;


import anders.olsen.api.entity.User;
import anders.olsen.api.entity.VerifyToken;
import anders.olsen.api.repository.VerifyEmailTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    private VerifyEmailTokenRepository verifyRepo;

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

    /**
     * Sending verification e-mail to user's email-address
     *
     * @param user user's email address
     */
    @Override
    public void sendVerifyEmail(User user) {
        final String url = "localhost:5000/api/auth/verify/";
        VerifyToken verifyToken;

        Optional<VerifyToken> opt = verifyRepo.getById(user.getId());


        if (!opt.isPresent())
            verifyToken = new VerifyToken(UUID.randomUUID().toString(), user);
        else {
            verifyToken = opt.get();
            verifyToken.setToken(UUID.randomUUID().toString());
        }

        verifyRepo.save(verifyToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("support@olsenapi.com");
        message.setTo(user.getEmail());
        message.setSubject("Verify email");
        message.setText("Press this link to verify your email: " + url + verifyToken.getToken());

        sendEmail(message);
    }

}
