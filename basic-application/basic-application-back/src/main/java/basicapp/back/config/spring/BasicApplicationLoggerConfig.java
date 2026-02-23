package basicapp.back.config.spring;

import igloo.loginmdc.annotation.LogExecutionContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationLoggerConfig {

  @Bean
  public LogExecutionContributor loginContributor() {
    return new LogExecutionContributor();
  }
}
