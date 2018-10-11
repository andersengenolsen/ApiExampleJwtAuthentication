package anders.olsen.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for automatically populating createdBy and updatedBy columns for audited entities.
 */
@Configuration
@EnableJpaAuditing
public class AuditConfig {
    // TODO: Implement audit configuration
}
