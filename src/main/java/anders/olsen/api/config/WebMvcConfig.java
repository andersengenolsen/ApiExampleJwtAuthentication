package anders.olsen.api.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Custom web configuration class
 *
 * @author Anders Engen Olsen
 */
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE = 3600;

    /**
     * Enabling cross origin requests globally, for accessing the API
     * from another server.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE);
    }


}
