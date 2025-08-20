package test.wicket.more.config;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test.wicket.more.link.descriptor.application.WicketMoreTestLinkDescriptorApplication;

/** Stub. */
@Configuration
public class TestLinkDescriptorApplicationConfiguration {

  @Bean
  public WebApplication application() {
    return new WicketMoreTestLinkDescriptorApplication();
  }
}
