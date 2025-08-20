package test.core.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.core.TestEntityDatabaseHelper;

@Configuration
public class BasicApplicationBackTestBaseConfiguration {

  @Bean
  public TestEntityDatabaseHelper entityDatabaseHelper() {
    return new TestEntityDatabaseHelper();
  }
}
