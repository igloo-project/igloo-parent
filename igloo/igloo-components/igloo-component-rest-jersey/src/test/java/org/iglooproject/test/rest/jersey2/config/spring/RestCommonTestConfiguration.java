package org.iglooproject.test.rest.jersey2.config.spring;

import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import java.util.List;
import org.glassfish.jersey.servlet.ServletContainer;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.iglooproject.test.rest.jersey2.business.RestTestBusinessPackage;
import org.iglooproject.test.rest.jersey2.client.RestClientPackage;
import org.iglooproject.test.rest.jersey2.server.SimpleRestApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@Configuration
@Import({JpaOnlyTestConfiguration.class, PsqlTestContainerConfiguration.class})
@EntityScan(basePackageClasses = RestTestBusinessPackage.class)
@ComponentScan(basePackageClasses = {RestTestBusinessPackage.class, RestClientPackage.class})
@EnableTransactionManagement
public class RestCommonTestConfiguration {

  @Configuration
  public static class WebXmlConfiguration {

    @Bean
    public ServletRegistrationBean<ServletContainer> restServlet() {
      ServletContainer servletContainer = new ServletContainer(new SimpleRestApplication());
      ServletRegistrationBean<ServletContainer> bean = new ServletRegistrationBean<>();
      bean.setServlet(servletContainer);
      bean.setName("restServlet");
      bean.setUrlMappings(List.of("/*"));

      return bean;
    }
  }
}
