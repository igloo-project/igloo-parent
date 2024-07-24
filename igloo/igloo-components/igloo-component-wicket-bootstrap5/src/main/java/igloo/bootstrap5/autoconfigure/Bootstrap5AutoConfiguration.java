package igloo.bootstrap5.autoconfigure;

import igloo.bootstrap5.application.WicketBootstrap5Module;
import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.wicket.more.autoconfigure.WicketMoreAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    name = "igloo-ac.bootstrap5.disabled",
    havingValue = "false",
    matchIfMissing = true)
@ConditionalOnClass(value = WicketBootstrap5Module.class)
@AutoConfigureAfter({WicketMoreAutoConfiguration.class})
@ConditionalOnBean(WebApplication.class)
public class Bootstrap5AutoConfiguration {
  @Bean
  public WicketBootstrap5Module bootstrap5Module() {
    return new WicketBootstrap5Module();
  }
}
