package rocket.planet.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
 * JPA Audit 설정
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "rocket.planet.repository.jpa")
public class JpaAuditConfig {
}
