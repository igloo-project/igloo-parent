package fr.openwide.jpa.example.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.spring.AbstractApplicationConfig;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;
import fr.openwide.jpa.example.business.CoreJpaExampleBusinessPackage;

@Configuration
@ApplicationDescription(name = "owsi-core-jpa-example")
@ConfigurationLocations(locations = {
		"classpath:owsi-core-component-jpa.properties",
		"classpath:owsi-hibernate.properties"
})
@Import(CoreJpaExampleJpaConfig.class)
@ComponentScan(
		basePackageClasses = CoreJpaExampleBusinessPackage.class,
		excludeFilters = @Filter(Configuration.class)
)
public class CoreJpaExampleConfig extends AbstractApplicationConfig {

	@Bean
	public static CoreConfigurer configurer() {
		return new CoreConfigurer();
	}

}
