package basicapp.app.config;

import basicapp.back.config.spring.BasicApplicationBackBaseConfiguration;
import basicapp.back.config.spring.BasicApplicationBackFlywayJavaMigrationConfiguration;
import basicapp.back.config.spring.BasicApplicationBackJpaConfiguration;
import basicapp.back.config.spring.BasicApplicationBackManifestConfiguration;
import basicapp.back.config.spring.BasicApplicationBackNotificationConfiguration;
import basicapp.back.config.spring.BasicApplicationBackPropertyRegistryConfiguration;
import basicapp.back.config.spring.BasicApplicationBackReferenceDataConfiguration;
import basicapp.back.config.spring.BasicApplicationBackSchedulingConfiguration;
import basicapp.back.config.spring.BasicApplicationBackSecurityConfiguration;
import basicapp.back.config.spring.BasicApplicationBackTaskManagementConfiguration;
import basicapp.front.BasicApplicationApplication;
import basicapp.front.config.BasicApplicationFrontNotificationConfiguration;
import basicapp.front.config.BasicApplicationFrontPropertyRegistryConfiguration;
import basicapp.front.config.BasicApplicationFrontSecurityConfiguration;
import basicapp.front.config.BasicApplicationFrontServletConfiguration;
import basicapp.front.config.BasicApplicationFrontWicketConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  BasicApplicationBackBaseConfiguration.class,
  BasicApplicationBackManifestConfiguration.class,
  BasicApplicationBackJpaConfiguration.class,
  BasicApplicationBackPropertyRegistryConfiguration.class,
  BasicApplicationBackSecurityConfiguration.class,
  BasicApplicationBackFlywayJavaMigrationConfiguration.class,
  BasicApplicationBackSchedulingConfiguration.class,
  BasicApplicationBackTaskManagementConfiguration.class,
  BasicApplicationBackNotificationConfiguration.class,
  BasicApplicationBackReferenceDataConfiguration.class,
  BasicApplicationFrontPropertyRegistryConfiguration.class,
  BasicApplicationFrontSecurityConfiguration.class,
  BasicApplicationFrontServletConfiguration.class,
  BasicApplicationFrontWicketConfiguration.class,
  BasicApplicationFrontNotificationConfiguration.class
})
@ComponentScan(
    basePackageClasses = {BasicApplicationApplication.class},
    excludeFilters = @Filter(Configuration.class))
public class BasicApplicationApplicationMainConfiguration {}
