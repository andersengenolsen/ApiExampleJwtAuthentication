package anders.olsen.api.payload;

import java.time.Instant;

/**
 * Class representing a user profile.
 * Audit columns are included.
 */
public class UserProfile {

    private String username;
    private String firstName;
    private String lastName;
    private Instant joinedAt;

    // TODO: Implement builder pattern!
    public UserProfile(String username, String firstName,
                       String lastName, Instant joinedAt) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinedAt = joinedAt;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }
}
