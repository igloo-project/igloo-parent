package test.jpa.more.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.APPLICATION,
    value = {
      ConfigurationPropertiesUrlConstants.JPA_COMMON,
      ConfigurationPropertiesUrlConstants.JPA_SEARCH_LUCENE_COMMON,
      "classpath:jpa-more-test.properties",
      "classpath:property-test.properties",
    },
    encoding = "UTF-8")
@Import({JpaMoreTestJpaConfig.class, JpaMoreTestApplicationPropertyConfig.class})
@ComponentScan(basePackageClasses = {JpaMoreTestBusinessPackage.class})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class JpaMoreTestConfig extends AbstractApplicationConfig {

  @Bean
  public IRendererService rendererService() {
    return new EmptyRendererServiceImpl();
  }
}
