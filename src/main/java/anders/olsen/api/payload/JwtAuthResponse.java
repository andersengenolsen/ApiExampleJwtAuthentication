package anders.olsen.api.payload;

/**
 * Defining payload payloads for JWT authentication
 */
public class JwtAuthResponse {

    /**
     * Access token returned to the user
     */
    private String accessToken;
    /**
     * Type = bearer.
     */
    private String tokenType = "Bearer";

    /**
     * Http successful status
     */
    private int status = 200;

    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

