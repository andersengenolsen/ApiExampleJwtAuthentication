package anders.olsen.api.repository;

import anders.olsen.api.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repo for accessing reset password tokens.
 *
 * {@link ResetToken}
 */
@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetToken, Long> {

    Optional<ResetToken> findByToken(String token);
}
