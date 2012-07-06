package fr.openwide.core.test.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.spring.AbstractApplicationConfig;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;
import fr.openwide.core.test.jpa.example.business.JpaTestBusinessPackage;

@Configuration
@ApplicationDescription(name = "jpa-test")
@ConfigurationLocations(locations = {
		"classpath:owsi-core-component-jpa.properties",
		"classpath:configuration-private.properties",
		"classpath:owsi-hibernate.properties"
})
@Import(JpaTestJpaConfig.class)
@ComponentScan(
		basePackageClasses = JpaTestBusinessPackage.class,
		excludeFilters = @Filter(Configuration.class)
)
public class JpaTestConfig extends AbstractApplicationConfig {

	@Bean
	public static CoreConfigurer configurer() {
		return new CoreConfigurer();
	}

}
