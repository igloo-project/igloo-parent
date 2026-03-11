package basicapp.back.config;

import com.google.common.collect.Lists;
import db.migration.standalone.JavaMigrationStandalonePackage;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnProperty(name = "custom.flyway.mode", havingValue = "standalone")
public class BasicApplicationBackFlywayStandaloneConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
    return configuration -> {
      List<String> locations = Lists.newArrayList();
      locations.add("db/migration/snapshot/**/*.sql");
      locations.add("db/migration/standalone/auto/**/*.sql");
      configuration.locations(locations.toArray(String[]::new));
    };
  }

  @Configuration
  @ComponentScan(basePackageClasses = JavaMigrationStandalonePackage.class)
  public static class JavaMigrationConfiguration {}
}
