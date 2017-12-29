package org.iglooproject.test.rest.jersey2.server.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.rest.jersey2.business.RestTestBusinessPackage;
import org.iglooproject.test.rest.jersey2.server.RestServerPackage;

@Configuration
@ApplicationDescription(name = "rest-test-server")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		"classpath:configuration-private.properties",
		"classpath:rest-server.properties"
})
@Import({
	RestServerTestJpaConfig.class,
	RestServerTestApplicationPropertyConfig.class
})
@ComponentScan(
		basePackageClasses = { RestTestBusinessPackage.class, RestServerPackage.class },
		excludeFilters = @Filter(Configuration.class)
)
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class RestServerTestCoreCommonConfig extends AbstractApplicationConfig {
}