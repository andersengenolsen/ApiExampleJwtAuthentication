package anders.olsen.api.entity;

import anders.olsen.api.entity.audit.CreatedUpdatedAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Token used for email verification for new users.
 * <p>
 * Token is valid for one hour, sent by mail to the users email.
 */
@Entity
@Table(name = "verify_token")
public class VerifyToken extends CreatedUpdatedAudit {

    @Id
    private Long id;

    @NotBlank
    private String token;

    @JoinColumn(name = "user_id", unique = true)
    @OneToOne
    @MapsId
    private User user;

    public VerifyToken() {
    }

    public VerifyToken(@NotBlank String token, User user) {
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
}
