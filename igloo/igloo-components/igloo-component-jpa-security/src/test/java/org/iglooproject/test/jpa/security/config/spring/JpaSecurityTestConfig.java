package org.iglooproject.test.jpa.security.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.igloo.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.jpa.security.business.JpaSecurityTestBusinessPackage;

@Configuration
@ApplicationDescription(name = "igloo-component-security")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		ConfigurationPropertiesUrlConstants.JPA_SECURITY_COMMON,
		"classpath:jpa-security-test.properties"
})
@ComponentScan(basePackageClasses = {
		JpaSecurityTestBusinessPackage.class
})
@Import({
	JpaSecurityTestJpaConfig.class,
	JpaSecurityTestSecurityConfig.class,
	JpaSecurityTestApplicationPropertyConfig.class
})
public class JpaSecurityTestConfig extends AbstractApplicationConfig {
	
	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

}
