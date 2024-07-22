package test.jpa.more.config.spring;

import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@EnableAutoConfiguration
@ComponentScan(
    basePackageClasses = {
      CoreJpaMoreUtilTransactionPackage.class,
      JpaMoreTestBusinessPackage.class
    })
@EntityScan(basePackageClasses = {JpaMoreTestBusinessPackage.class})
public class TestConfig {
  @Bean
  public IRendererService rendererService() {
    return new EmptyRendererServiceImpl();
  }
}
