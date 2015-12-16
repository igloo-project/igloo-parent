package fr.openwide.core.test.jpa.externallinkchecker.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.externallinkchecker.business.JpaExternalLinkCheckerBusinessPackage;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.spring.AbstractApplicationConfig;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;

@Configuration
@ApplicationDescription(name = "owsi-component-jpa-more")
@ConfigurationLocations(locations = {
		"classpath:owsi-core-component-jpa.properties",
		"classpath:jpa-externallinkchecker-test.properties",
		"classpath:externallinkchecker-test.properties"
})
@Import(JpaExternalLinkCheckerTestJpaConfig.class)
@ComponentScan(basePackageClasses = { JpaExternalLinkCheckerBusinessPackage.class })
@EnableAspectJAutoProxy
public class JpaExternalLinkCheckerTestConfig extends AbstractApplicationConfig {

	@Bean
	public static CoreConfigurer configurer() {
		return new CoreConfigurer();
	}

}
