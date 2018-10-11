package anders.olsen.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true, // Enabling @Secure annotation on method level
        jsr250Enabled = true, // Enabling @RolesAllowed annotation on method level
        prePostEnabled = true // Enabling @PreAuthorize and @PostAuthorize expressions
)
/**
 * Global security config class.
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO: Define global security
}
