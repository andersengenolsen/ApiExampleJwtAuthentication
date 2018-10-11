package anders.olsen.api.security;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Helper class for generation of JWT after users have logged in.
 * <p>
 * Performing validation on the JWT sent in the Auth header.
 * <p>
 * Reading JWT secret and expiration time from application.properties
 *
 * @author Anders Engen Olsen
 */
@Component
public class TokenProvider {

    private static final Logger log =
            LoggerFactory.getLogger(TokenProvider.class);

    /**
     * JWT key from application.properties
     */
    @Value("${app.jwtSecret}")
    private String jwtKey;

    /**
     * JWT expiration time
     */
    @Value("${app.jwtExpirationInMs}")
    private int jwtExp;

    /**
     * Generating JWT token.
     *
     * @param authentication
     * @return JWT token
     */
    public String generateToken(Authentication authentication) {
        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        // Setting expiration date
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExp);

        return Jwts.builder()
                .setSubject(Long.toString(customUserPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtKey)
                .compact();
    }

    /**
     * Fetcing user id by given JWT token
     *
     * @param token JWT token
     * @return User ID with given token
     */
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validating JWT token
     *
     * @param authToken JWT token
     * @return True if token is valid.
     */
    public boolean validateToken(String authToken) {

        String message = "";

        try {
            Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(authToken);
            message = "Token validated";
            return true;
        } catch (SignatureException ex) {
            message = "Invalid signature";
        } catch (MalformedJwtException ex) {
            message = "Invalid token";
        } catch (ExpiredJwtException ex) {
            message = "Expired token";
        } catch (UnsupportedJwtException ex) {
            message = "Unsupported token";
        } catch (IllegalArgumentException ex) {
            message = "Empty token";
        } finally {
            log.debug(message);
        }

        return false;
    }
}
