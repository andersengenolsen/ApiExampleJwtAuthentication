package anders.olsen.api.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class will get the JWT token from a request, validate it, load the associated user,
 * and finally pass that user to Spring Security! Phew....
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Logging
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /**
     * {@link TokenProvider}, for validating tokens.
     */
    @Autowired
    private TokenProvider tokenProvider;

    /**
     * {@link CustomUserDetailsService} for looking up users in the database.
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Fetching JWT from HttpServletRequest.
     * If the token is valid, the tokenProvider will find the user's id.
     * The customUserDetailsService will then load the UserDetails by that id.
     *
     * @see #getJwtFromRequest(HttpServletRequest)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Parsing JWT token from request
            String jwt = getJwtFromRequest(request);

            // Valid token?
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                // Fetching the id
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                // Loading user by given ID
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);

                // Passing authentication to Spring Security Context
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Retrieving JWT token from a request
     *
     * @param request HTTP request
     * @return JWT token in Authorization header
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

