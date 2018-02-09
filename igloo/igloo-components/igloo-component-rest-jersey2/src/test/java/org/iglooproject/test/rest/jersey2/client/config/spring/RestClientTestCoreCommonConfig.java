package org.iglooproject.test.rest.jersey2.client.config.spring;

import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.rest.jersey2.business.RestTestBusinessPackage;
import org.iglooproject.test.rest.jersey2.client.RestClientPackage;
import org.iglooproject.test.rest.jersey2.context.MockJpaRestServlet;
import org.iglooproject.test.web.context.MockServlet;
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
	public MockServlet restServerTestResource(@Value("${jersey.mock.http.port}") Integer httpPort) {
		return new MockJpaRestServlet(
				"http://localhost/", httpPort, "/api", "/rest");
	}
}