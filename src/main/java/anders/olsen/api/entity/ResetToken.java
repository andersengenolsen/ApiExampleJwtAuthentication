package anders.olsen.api.entity;

import anders.olsen.api.entity.audit.CreatedUpdatedAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Entity representing a reset token for users resetting passwords.
 * <p>
 * Invalidated after one hour!
 *
 * @see #isValid()
 */
@Entity
@Table(name = "reset_token")
public class ResetToken extends CreatedUpdatedAudit {

    @Id
    private Long id;

    @NotBlank
    private String token;

    @JoinColumn(name = "user_id", unique = true)
    @OneToOne
    @MapsId
    private User user;

    public ResetToken() {
    }

    public ResetToken(@NotBlank String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return true if less than one hours since modified.
     */
    public boolean isValid() {
        Instant timeout = getUpdatedTime().plus(1, ChronoUnit.HOURS);
        return Instant.now().isBefore(timeout);
    }
}
