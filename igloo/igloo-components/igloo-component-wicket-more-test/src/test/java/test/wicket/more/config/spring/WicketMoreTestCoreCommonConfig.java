package test.wicket.more.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.wicket.core.config.spring.WicketTestApplicationPropertyConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import test.wicket.more.business.WicketMoreTestBusinessPackage;

@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.APPLICATION,
    value = ConfigurationPropertiesUrlConstants.JPA_COMMON,
    encoding = "UTF-8")
@Import({WicketMoreTestJpaConfig.class, WicketTestApplicationPropertyConfig.class})
@ComponentScan(
    basePackageClasses = WicketMoreTestBusinessPackage.class,
    excludeFilters = @Filter(Configuration.class))
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class WicketMoreTestCoreCommonConfig extends AbstractApplicationConfig {}
