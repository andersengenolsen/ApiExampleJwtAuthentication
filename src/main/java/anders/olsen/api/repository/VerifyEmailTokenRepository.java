package anders.olsen.api.repository;

import anders.olsen.api.entity.VerifyToken;
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

    Optional<VerifyToken> findByToken(String token);
}
