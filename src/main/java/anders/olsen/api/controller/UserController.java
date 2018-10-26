package anders.olsen.api.controller;

import anders.olsen.api.entity.User;
import anders.olsen.api.payload.UserProfile;
import anders.olsen.api.payload.UserSummary;
import anders.olsen.api.repository.UserRepository;
import anders.olsen.api.security.CurrentUser;
import anders.olsen.api.security.CustomUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Endpoint for user section of the api, /api/users.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

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
    public UserSummary updateUserDetails(@CurrentUser CustomUserPrincipal userPrincipal) {
        // TODO: Update user information
        return null;
    }


    /* -- AUTOWIRED SETTERS -- */
    @Autowired
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
}
