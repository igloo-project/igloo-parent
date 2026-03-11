package basicapp.back.config;

import com.google.common.collect.Lists;
import db.migration.production.JavaMigrationProductionPackage;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnProperty(name = "custom.flyway.mode", havingValue = "production")
public class BasicApplicationBackFlywayProductionConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
    return configuration -> {
      List<String> locations = Lists.newArrayList();
      locations.add("db/migration/production/auto/**/*.sql");
      configuration.locations(locations.toArray(String[]::new));
    };
  }

  @Configuration
  @ComponentScan(basePackageClasses = JavaMigrationProductionPackage.class)
  public static class JavaMigrationConfiguration {}
}
