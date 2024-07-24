package org.iglooproject.test.rest.jersey2.config.spring;

import org.iglooproject.test.rest.jersey2.client.RestClientPackage;
import org.iglooproject.test.rest.jersey2.server.MockServlet;
import org.iglooproject.test.web.context.AbstractMockServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RestCommonTestConfiguration.class)
@ComponentScan(basePackageClasses = RestClientPackage.class)
public class RestClientTestConfiguration {

  @Bean
  public AbstractMockServlet restServerTestResource(
      @Value("${jersey.mock.http.port}") Integer httpPort) {
    return new MockServlet("http://localhost/", httpPort, "/api", "/rest");
  }
}
