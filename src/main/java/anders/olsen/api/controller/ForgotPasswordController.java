package anders.olsen.api.controller;

import anders.olsen.api.entity.ResetToken;
import anders.olsen.api.entity.User;
import anders.olsen.api.payload.ApiResponse;
import anders.olsen.api.payload.ResetPassword;
import anders.olsen.api.payload.ResetRequest;
import anders.olsen.api.repository.ResetPasswordTokenRepository;
import anders.olsen.api.repository.UserRepository;
import anders.olsen.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for handling forgot password actions
 */
@RestController
@RequestMapping("/api/reset")
public class ForgotPasswordController {

    /**
     * For sending email
     */
    private EmailService emailService;

    /**
     * Accessing users
     */
    private UserRepository userRepository;

    /**
     * Accessing tokens
     */
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    /**
     * Password encryption
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Handling post against /reset, containing usernameOrEmail.
     * <p>
     * If user exists, a token is generated and saved to the database.
     * An email is sent to the registered email, containing url to reset password.
     * <p>
     * Only sending the token by mail.
     * In production one would have to send the entire url.
     *
     * @param resetRequest reset password
     * @return 200 OK if user exists
     */
    @PostMapping()
    public ResponseEntity<?> generateResetToken(@Valid @RequestBody ResetRequest resetRequest) {

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(
                resetRequest.getUsernameOrEmail(), resetRequest.getUsernameOrEmail());

        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found!");
        }

        // Generating random reset token, saving to database
        User user = optionalUser.get();
        Optional<ResetToken> optionalResetToken = resetPasswordTokenRepository.findById(user.getId());

        ResetToken resetToken;

        // If user already has a token, create new one
        if (optionalResetToken.isPresent()) {
            resetToken = optionalResetToken.get();
            resetToken.setToken(UUID.randomUUID().toString());
        } else {
            resetToken = new ResetToken(UUID.randomUUID().toString(), user);
        }
        resetPasswordTokenRepository.save(resetToken);


        // Generating email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("support@olsenapi.com");
        message.setTo(user.getEmail());
        message.setSubject("Password reset");
        message.setText("Reset password with token: " + resetToken.getToken());

        emailService.sendEmail(message);

        return ResponseEntity.ok().body(new ApiResponse(true, "Reset email sent to "
                + user.getEmail()
                , HttpServletResponse.SC_OK));
    }

    /**
     * Verifying token, and changing password.
     * <p>
     * Clearing the token after the reset action has been performed!
     *
     * @param resetPassword {@link ResetPassword}
     * @return 200 OK if reset
     */
    @PostMapping("/newpassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ResetPassword resetPassword) {

        // Passwords not matching
        if (!resetPassword.getPassword().equals(resetPassword.getPasswordVerif()))
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Password fields " +
                    "must match", HttpServletResponse.SC_BAD_REQUEST));

        // Fetching token and user
        Optional<ResetToken> opt = resetPasswordTokenRepository.findByToken(resetPassword.getToken());

        // Invalid token, ie. user not found by token
        if (!opt.isPresent() || opt.get().getUser() == null)
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid token",
                    HttpServletResponse.SC_BAD_REQUEST));

        ResetToken resetToken = opt.get();

        // Checking that reset token is valid
        if (!resetToken.isValid())
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid token",
                    HttpServletResponse.SC_BAD_REQUEST));


        // Updating password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        userRepository.save(user);

        // Deleting token
        resetPasswordTokenRepository.delete(resetToken);

        // return OK
        return ResponseEntity.ok().body(new ApiResponse(true, "Password updated",
                HttpServletResponse.SC_OK));

    }

    /* -- AUTOWIRED SETTERS & GETTERS -- */

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setbCryptPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setResetPasswordTokenRepository(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }
}
