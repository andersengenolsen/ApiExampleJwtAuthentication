package anders.olsen.api.payload;

/**
 * Helper class, is username / email available?
 */
public class UserAvailability {

    private Boolean available;

    public UserAvailability(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

