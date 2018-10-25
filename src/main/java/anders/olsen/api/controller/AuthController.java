package anders.olsen.api.controller;

import anders.olsen.api.entity.Role;
import anders.olsen.api.entity.RoleName;
import anders.olsen.api.entity.User;
import anders.olsen.api.entity.VerifyToken;
import anders.olsen.api.exception.AppException;
import anders.olsen.api.payload.ApiResponse;
import anders.olsen.api.payload.JwtAuthResponse;
import anders.olsen.api.payload.LoginRequest;
import anders.olsen.api.payload.SignUpRequest;
import anders.olsen.api.repository.RoleRepository;
import anders.olsen.api.repository.UserRepository;
import anders.olsen.api.repository.VerifyEmailTokenRepository;
import anders.olsen.api.security.TokenProvider;
import anders.olsen.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Authentication Controller,
 * containing APIs for logging in and signing up to the
 * application.
 * <p>
 * Mapped to /api/auth.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * {@link AuthenticationManager}, spring security default authentication.
     * Custom settings provided in {@link anders.olsen.api.config.SecurityConfig}
     */
    private AuthenticationManager authenticationManager;

    /**
     * Accessing Users in the database. {@link anders.olsen.api.security.CustomUserDetailsService}
     */
    private UserRepository userRepository;

    /**
     * Accessing roles in the database
     */
    private RoleRepository roleRepository;

    /**
     * BCrypt password encoder
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Providing and validating tokens.
     */
    private TokenProvider tokenProvider;

    /**
     * Verify email tokens in registration step
     */
    private VerifyEmailTokenRepository verifyRepo;

    /**
     * Email sending
     */
    private EmailService emailService;

    /**
     * Handling requests for signing in to the application.
     * Signing in user, and returning a valid JWT token.
     *
     * @param loginRequest Valid request to log in, {@link LoginRequest}
     * @return 200 OK, valid JWT token.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Authenticating.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generating token
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

    /**
     * Handling requests for signing up to the application.
     *
     * @param signUpRequest Valid request to sign up, {@link SignUpRequest}
     * @return {@link ApiResponse}
     * @throws AppException if user role is not set.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        // Returning ApiResponse if user already exists with given username
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Username is taken!",
                            HttpServletResponse.SC_BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }

        // Returning ApiResponse if email already exists.
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Email is taken!",
                            HttpServletResponse.SC_BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating the user account.
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), signUpRequest.getUsername(),
                signUpRequest.getPassword());

        // Setting bcrypt encoded password.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Setting user role, throwing exception if role is not set.
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        // Sending email verification
        sendVerifyEmail(user);

        return ResponseEntity.created(location).body(new ApiResponse(
                true, "User registered successfully", HttpServletResponse.SC_OK));
    }

    /**
     * Verifying email address by token.
     *
     * @param token token provided in url
     */
    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {

        Optional<VerifyToken> tokenOptional = verifyRepo.findByToken(token);

        if(!tokenOptional.isPresent()) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid token!",
                            HttpServletResponse.SC_BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }

        VerifyToken verifyToken = tokenOptional.get();

        // Setting user to verified
        User user = verifyToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        // Cleaning up, removing verify token.
        verifyRepo.delete(verifyToken);

        // return OK
        return ResponseEntity.ok().body(new ApiResponse(true, "Email verified",
                HttpServletResponse.SC_OK));
    }

    /**
     * Helper method for sending a verification email to the user.
     * <p>
     * Generating token for validating email, send link to verify.
     *
     * @param user newly registered user.
     */
    private void sendVerifyEmail(User user) {

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

    /* -- Autowired setters for private fields -- */

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Autowired
    public void setVerifyRepo(VerifyEmailTokenRepository verifyRepo) {
        this.verifyRepo = verifyRepo;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}

