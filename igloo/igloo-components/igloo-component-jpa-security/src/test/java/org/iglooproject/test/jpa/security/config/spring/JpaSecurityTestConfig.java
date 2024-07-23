package org.iglooproject.test.jpa.security.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.jpa.security.business.JpaSecurityTestBusinessPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.APPLICATION,
    value = {
      ConfigurationPropertiesUrlConstants.JPA_COMMON,
      ConfigurationPropertiesUrlConstants.JPA_SECURITY_COMMON,
      "classpath:jpa-security-test.properties"
    },
    encoding = "UTF-8")
@ComponentScan(basePackageClasses = {JpaSecurityTestBusinessPackage.class})
@Import({
  JpaSecurityTestJpaConfig.class,
  JpaSecurityTestSecurityConfig.class,
  JpaSecurityTestApplicationPropertyConfig.class
})
public class JpaSecurityTestConfig extends AbstractApplicationConfig {

  @Bean
  public IRendererService rendererService() {
    return new EmptyRendererServiceImpl();
  }
}
