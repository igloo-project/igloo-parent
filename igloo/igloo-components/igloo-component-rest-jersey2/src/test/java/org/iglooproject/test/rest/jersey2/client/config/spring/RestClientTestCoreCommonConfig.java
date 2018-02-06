package org.iglooproject.test.rest.jersey2.client.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.igloo.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.rest.jersey2.business.RestTestBusinessPackage;
import org.iglooproject.test.rest.jersey2.client.RestClientPackage;

@Configuration
@ApplicationDescription(name = "rest-test-client")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		"classpath:rest-client.properties"
})
@Import({
	RestClientTestJpaConfig.class,
	RestClientTestApplicationPropertyConfig.class
})
@ComponentScan(
		basePackageClasses = { RestTestBusinessPackage.class, RestClientPackage.class },
		excludeFilters = @Filter(Configuration.class)
)
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class RestClientTestCoreCommonConfig extends AbstractApplicationConfig {
}