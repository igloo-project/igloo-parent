package org.iglooproject.test.rest.jersey2.client.config.spring;

import org.glassfish.jersey.server.ResourceConfig;
import org.igloo.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.rest.jersey2.SimpleRestApplication;
import org.iglooproject.test.rest.jersey2.business.RestTestBusinessPackage;
import org.iglooproject.test.rest.jersey2.client.RestClientPackage;
import org.iglooproject.test.rest.jersey2.context.RestServerTestResource;
import org.iglooproject.test.rest.jersey2.server.config.spring.RestServerTestCoreCommonConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ApplicationDescription(name = "rest-test-client")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		ConfigurationPropertiesUrlConstants.JERSEY_MOCK_COMMON,
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
@EnableTransactionManagement
public class RestClientTestCoreCommonConfig extends AbstractApplicationConfig {
	
	@Bean
	public RestServerTestResource restServerTestResource(@Value("${jersey.mock.http.port}") Integer httpPort) {
		return new RestServerTestResource(
				"http://localhost/", httpPort, "/api", "/rest", RestServerTestCoreCommonConfig.class) {
			@Override
			protected ResourceConfig createApplication() {
				return new SimpleRestApplication();
			}
		};
	}
}