package basicapp.front.config;

import basicapp.front.BasicApplicationApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@Import(BasicApplicationFrontWicketFilterConfiguration.class)
public class BasicApplicationFrontWicketConfiguration {

  @Bean(name = {"BasicApplicationApplication", "application"})
  public BasicApplicationApplication application() {
    return new BasicApplicationApplication();
  }
}
