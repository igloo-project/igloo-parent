package org.iglooproject.test.jpa.externallinkchecker.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.externallinkchecker.business.JpaExternalLinkCheckerBusinessPackage;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.jpa.externallinkchecker.business.rest.server.MockServlet;
import org.iglooproject.test.web.context.AbstractMockServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = IglooPropertySourcePriority.APPLICATION,
	value = {
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		ConfigurationPropertiesUrlConstants.JERSEY_MOCK_COMMON,
		"classpath:externallinkchecker-test.properties"
	}
)
@Import({
	JpaExternalLinkCheckerTestJpaConfig.class,
	JpaExternalLinkCheckerTestApplicationPropertyConfig.class
})
@ComponentScan(basePackageClasses = { JpaExternalLinkCheckerBusinessPackage.class })
@EnableAspectJAutoProxy
public class JpaExternalLinkCheckerTestConfig extends AbstractApplicationConfig {
	
	@Bean
	public AbstractMockServlet restServerTestResource(@Value("${jersey.mock.http.port}") Integer httpPort) {
		return new MockServlet("http://localhost/", httpPort, "/api", "/rest");
	}

}
