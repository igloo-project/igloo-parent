package org.iglooproject.test.jpa.externallinkchecker.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import org.iglooproject.jpa.externallinkchecker.business.JpaExternalLinkCheckerBusinessPackage;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;

@Configuration
@ApplicationDescription(name = "igloo-component-jpa-more")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		"classpath:jpa-externallinkchecker-test.properties",
		"classpath:externallinkchecker-test.properties"
})
@Import({
	JpaExternalLinkCheckerTestJpaConfig.class,
	JpaExternalLinkCheckerTestApplicationPropertyConfig.class
})
@ComponentScan(basePackageClasses = { JpaExternalLinkCheckerBusinessPackage.class })
@EnableAspectJAutoProxy
public class JpaExternalLinkCheckerTestConfig extends AbstractApplicationConfig {

}
