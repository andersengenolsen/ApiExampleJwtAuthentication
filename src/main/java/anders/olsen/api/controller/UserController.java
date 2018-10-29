package anders.olsen.api.controller;

import anders.olsen.api.entity.User;
import anders.olsen.api.payload.ApiResponse;
import anders.olsen.api.payload.UpdateUserRequest;
import anders.olsen.api.payload.UserProfile;
import anders.olsen.api.payload.UserSummary;
import anders.olsen.api.repository.UserRepository;
import anders.olsen.api.security.CurrentUser;
import anders.olsen.api.security.CustomUserPrincipal;
import anders.olsen.api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Endpoint for user section of the api, /api/users.
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(maxAge = 3600)
public class UserController {

    /**
     * For sending email
     */
    private EmailService emailService;

    /**
     * Accessing users in database
     */
    private UserRepository userRepo;

    /**
     * Returning {@link UserProfile} summary for a given username
     *
     * @param username username
     * @return {@link UserProfile}
     */
    @GetMapping("/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {

        Optional<User> opt = userRepo.findByUsername(username);

        if (!opt.isPresent())
            throw new UsernameNotFoundException("Username: " + username + " , not found");

        User user = opt.get();

        return new UserProfile(user.getUsername(),
                user.getFirstName(), user.getLastName(),
                user.getCreatedTime());
    }

    /**
     * Returning summary of currently logged in user.
     *
     * @param userPrincipal representation of user
     * @return {@link UserSummary}
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getPrivateUser(@CurrentUser CustomUserPrincipal userPrincipal) {
        return new UserSummary(userPrincipal.getId(), userPrincipal.getUsername(),
                userPrincipal.getFirstName(), userPrincipal.getLastName(),
                userPrincipal.getEmail());
    }

    /**
     * Currently logged in user can update their information.
     *
     * @param userPrincipal
     * @return UserSummary after update.
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserDetails(@CurrentUser CustomUserPrincipal userPrincipal,
                                               @RequestBody @Valid UpdateUserRequest updateUserRequest) {

        String prevUsername = userPrincipal.getUsername();
        String newUsername = updateUserRequest.getUsername();
        String prevEmail = userPrincipal.getEmail();
        String newEmail = updateUserRequest.getEmail();

        Optional<User> opt = userRepo.findByUsername(prevUsername);

        if (!opt.isPresent())
            throw new UsernameNotFoundException("Username not found");

        User user = opt.get();
        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());

        // Updating username?
        if (!prevUsername.equals(newUsername)) {
            // Returning ApiResponse if user already exists with given username
            if (userRepo.existsByUsername(newUsername)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Username is taken!",
                                HttpServletResponse.SC_BAD_REQUEST),
                        HttpStatus.BAD_REQUEST);
            }

            user.setUsername(newUsername);
        }

        // Updating email?
        if (!prevEmail.equals(newEmail)) {
            // Returning ApiResponse if email already exists.
            if (userRepo.existsByEmail(newEmail)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Email is taken!",
                                HttpServletResponse.SC_BAD_REQUEST),
                        HttpStatus.BAD_REQUEST);
            }

            user.setEmail(newEmail);
            user.setVerified(false);
            emailService.sendVerifyEmail(user);
        }

        userRepo.save(user);

        return ResponseEntity.ok().body(
                new ApiResponse(true, "User updated", HttpServletResponse.SC_OK));
    }


    /* -- AUTOWIRED SETTERS -- */
    @Autowired
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
