package test.web.config.spring;

import basicapp.front.config.spring.BasicApplicationWebappConfig;
import org.iglooproject.test.jpa.junit.PSQLTestContainerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import test.core.TestEntityDatabaseHelper;

@Configuration
@Import({BasicApplicationWebappConfig.class, PSQLTestContainerConfiguration.class})
public class BasicApplicationWebappTestCommonConfig {

  @Bean
  public TestEntityDatabaseHelper entityDatabaseHelper() {
    return new TestEntityDatabaseHelper();
  }
}
