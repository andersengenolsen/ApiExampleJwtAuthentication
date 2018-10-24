package anders.olsen.api.payload;

/**
 * Representing a request for resetting password for a given account.
 */
public class ResetRequest {

    private String usernameOrEmail;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
