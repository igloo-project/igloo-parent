package basicapp.back.config;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnProperty(name = "custom.flyway.mode", havingValue = "skeleton")
public class BasicApplicationBackFlywaySkeletonConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
    return configuration -> {
      List<String> locations = Lists.newArrayList();
      locations.add("db/migration/snapshot/**/*.sql");
      configuration.locations(locations.toArray(String[]::new));
    };
  }
}
