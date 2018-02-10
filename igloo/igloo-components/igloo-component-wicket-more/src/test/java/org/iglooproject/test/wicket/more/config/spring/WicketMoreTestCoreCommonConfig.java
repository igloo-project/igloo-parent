package org.iglooproject.test.wicket.more.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.wicket.more.business.WicketMoreTestBusinessPackage;

@Configuration
@ApplicationDescription(name = "wicket-more-test")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		ConfigurationPropertiesUrlConstants.JPA_COMMON
})
@Import({
	WicketMoreTestJpaConfig.class,
	WicketMoreTestApplicationPropertyConfig.class
})
@ComponentScan(
		basePackageClasses = WicketMoreTestBusinessPackage.class,
		excludeFilters = @Filter(Configuration.class)
)
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class WicketMoreTestCoreCommonConfig extends AbstractApplicationConfig {
}