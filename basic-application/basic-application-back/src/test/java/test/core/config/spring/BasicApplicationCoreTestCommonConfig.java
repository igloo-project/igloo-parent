package test.core.config.spring;

import basicapp.back.config.spring.BasicApplicationCoreCommonConfiguration;
import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import test.core.TestCorePackage;
import test.core.TestEntityDatabaseHelper;

@Configuration
@Import({BasicApplicationCoreCommonConfiguration.class, PsqlTestContainerConfiguration.class})
@ComponentScan(basePackageClasses = TestCorePackage.class)
public class BasicApplicationCoreTestCommonConfig {

  @Bean
  public IRendererService rendererService() {
    return new EmptyRendererServiceImpl();
  }

  @Bean
  public TestEntityDatabaseHelper entityDatabaseHelper() {
    return new TestEntityDatabaseHelper();
  }
}
