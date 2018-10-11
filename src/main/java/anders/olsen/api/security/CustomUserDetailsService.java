package anders.olsen.api.security;


import anders.olsen.api.entity.User;
import anders.olsen.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * User repository, accessing users in the database
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Loading a user by username OR email
     *
     * @param usernameOrEmail
     * @return {@link CustomUserPrincipal} representation of the user
     * @throws UsernameNotFoundException if email / username not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // Fetching user from the user repository, throwing ex. if not found
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found!"));

        return CustomUserPrincipal.create(user);
    }

    /**
     * Loading user by ID, used by the {@link JwtAuthenticationFilter}
     *
     * @param id user id
     * @return {@link CustomUserPrincipal} representation of the user.
     * @throws UsernameNotFoundException not found
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return CustomUserPrincipal.create(user);
    }
}
