package anders.olsen.api.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Representing post request to reset password
 */
public class ResetPassword {

    @NotBlank
    @Size(min = 6, max = 15)
    private String password;

    @NotBlank
    @Size(min = 6, max = 15)
    private String passwordVerif;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerif() {
        return passwordVerif;
    }

    public void setPasswordVerif(String passwordVerif) {
        this.passwordVerif = passwordVerif;
    }
}
