package test.web.config.spring;

import basicapp.front.config.spring.BasicApplicationWebappConfig;
import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import test.core.TestEntityDatabaseHelper;

@Configuration
@Import({BasicApplicationWebappConfig.class, PsqlTestContainerConfiguration.class})
public class BasicApplicationWebappTestCommonConfig {

  @Bean
  public TestEntityDatabaseHelper entityDatabaseHelper() {
    return new TestEntityDatabaseHelper();
  }
}
