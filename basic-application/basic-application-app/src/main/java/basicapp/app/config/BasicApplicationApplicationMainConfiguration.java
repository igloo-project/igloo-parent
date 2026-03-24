package basicapp.app.config;

import basicapp.back.config.BasicApplicationBackBaseConfiguration;
import basicapp.back.config.BasicApplicationBackDifferenceConfiguration;
import basicapp.back.config.BasicApplicationBackFlywayJavaMigrationConfiguration;
import basicapp.back.config.BasicApplicationBackJpaConfiguration;
import basicapp.back.config.BasicApplicationBackManifestConfiguration;
import basicapp.back.config.BasicApplicationBackNotificationConfiguration;
import basicapp.back.config.BasicApplicationBackPropertyRegistryConfiguration;
import basicapp.back.config.BasicApplicationBackReferenceDataConfiguration;
import basicapp.back.config.BasicApplicationBackSchedulingConfiguration;
import basicapp.back.config.BasicApplicationBackSecurityConfiguration;
import basicapp.back.config.BasicApplicationBackTaskManagementConfiguration;
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
  BasicApplicationBackDifferenceConfiguration.class,
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
