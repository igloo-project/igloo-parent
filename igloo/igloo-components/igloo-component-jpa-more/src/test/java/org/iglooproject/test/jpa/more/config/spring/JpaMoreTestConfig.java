package org.iglooproject.test.jpa.more.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.iglooproject.jpa.more.rendering.service.EmptyRendererServiceImpl;
import org.iglooproject.jpa.more.rendering.service.IRendererService;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@ApplicationDescription(name = "igloo-component-jpa-more")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		"classpath:jpa-more-test.properties",
		"classpath:property-test.properties"
})
@Import({
		JpaMoreTestJpaConfig.class,
		JpaMoreTestApplicationPropertyConfig.class
})
@ComponentScan(basePackageClasses = { JpaMoreTestBusinessPackage.class })
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class JpaMoreTestConfig extends AbstractApplicationConfig {

	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

}
