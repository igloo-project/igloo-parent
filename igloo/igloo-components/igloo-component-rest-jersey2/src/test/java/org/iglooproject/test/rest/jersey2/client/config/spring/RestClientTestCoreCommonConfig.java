package org.iglooproject.test.rest.jersey2.client.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.rest.jersey2.business.RestTestBusinessPackage;
import org.iglooproject.test.rest.jersey2.client.RestClientPackage;
import org.iglooproject.test.rest.jersey2.server.MockServlet;
import org.iglooproject.test.web.context.AbstractMockServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = {
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		ConfigurationPropertiesUrlConstants.JERSEY_MOCK_COMMON,
		"classpath:rest-client.properties"
	},
	encoding = "UTF-8"
)
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
	public AbstractMockServlet restServerTestResource(@Value("${jersey.mock.http.port}") Integer httpPort) {
		return new MockServlet("http://localhost/", httpPort, "/api", "/rest");
	}

}
