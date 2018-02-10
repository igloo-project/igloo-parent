package org.iglooproject.test.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.business.JpaTestBusinessPackage;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;

@Configuration
@ApplicationDescription(name = "jpa-test")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		"classpath:configuration-private.properties",
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		ConfigurationPropertiesUrlConstants.JPA_SEARCH_LUCENE_COMMON,
		"classpath:igloo-jpa.properties"
})
@Import({
	JpaTestJpaConfig.class,
	JpaTestApplicationPropertyConfig.class
})
@ComponentScan(
		basePackageClasses = JpaTestBusinessPackage.class,
		excludeFilters = @Filter(Configuration.class)
)
public class JpaTestConfig extends AbstractApplicationConfig {

}
