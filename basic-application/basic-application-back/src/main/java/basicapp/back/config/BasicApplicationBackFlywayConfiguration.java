package basicapp.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 3 available modes via the property {@code custom.flyway.mode}:
 *
 * <ul>
 *   <li>production: runs all migrations from a reference point (production DB dump).
 *   <li>standalone: init from scratch with schema + seed data.
 *   <li>skeleton: init from scratch with schema only (test).
 * </ul>
 */
@Configuration
@Import({
  BasicApplicationBackFlywayProductionConfiguration.class,
  BasicApplicationBackFlywayStandaloneConfiguration.class,
  BasicApplicationBackFlywaySkeletonConfiguration.class
})
public class BasicApplicationBackFlywayConfiguration {}
