package anders.olsen.api.repository;

import anders.olsen.api.entity.User;
import anders.olsen.api.entity.VerifyToken;
import anders.olsen.api.exception.InvalidTokenException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for accessing verify email tokens.
 * <p>
 * {@link anders.olsen.api.entity.VerifyToken}
 */
@Repository
public interface VerifyEmailTokenRepository extends JpaRepository<VerifyToken, Long> {

    /**
     * @param token Returning verify token
     * @return {@link VerifyToken}
     */
    Optional<VerifyToken> findByToken(String token);

    /**
     * Returning by ID
     *
     * @param id
     * @return
     */
    Optional<VerifyToken> getById(Long id);

    /**
     * Default implementation.
     * Returning user associated with a token.
     *
     * @param token email verification token
     * @return {@link User}
     * @throws InvalidTokenException if no token / user found.
     */
    default User findUserByToken(String token) {
        Optional<VerifyToken> opt = findByToken(token);

        opt.orElseThrow(() ->
                new InvalidTokenException("Invalid token!")
        );

        delete(opt.get());

        return opt.get().getUser();
    }

}
