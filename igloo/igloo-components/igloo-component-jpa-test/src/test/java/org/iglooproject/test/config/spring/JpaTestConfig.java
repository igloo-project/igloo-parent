package org.iglooproject.test.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.business.JpaTestBusinessPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ApplicationDescription(name = "jpa-test")
@PropertySource(name = IglooPropertySourcePriority.APPLICATION,
	value = {
		"classpath:igloo-component-jpa.properties",
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		ConfigurationPropertiesUrlConstants.JPA_SEARCH_LUCENE_COMMON,
		"classpath:igloo-jpa.properties"
	}
)
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
