package anders.olsen.api.controller;

import anders.olsen.api.entity.User;
import anders.olsen.api.payload.UserProfile;
import anders.olsen.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        UserProfile profile = new UserProfile(user.getId(), user.getUsername(),
                user.getFirstName(), user.getLastName(),
                user.getCreatedTime());

        return profile;

    }


    /* -- AUTOWIRED SETTERS -- */
    @Autowired
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
}
