package fr.openwide.core.test.jpa.more.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.more.rendering.service.EmptyRendererServiceImpl;
import fr.openwide.core.jpa.more.rendering.service.IRendererService;
import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.spring.AbstractApplicationConfig;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;
import fr.openwide.core.test.jpa.more.business.JpaMoreTestBusinessPackage;

@Configuration
@ApplicationDescription(name = "owsi-component-jpa-more")
@ConfigurationLocations(locations = {
		"classpath:owsi-core-component-jpa.properties",
		"classpath:jpa-more-test.properties",
		"classpath:property-test.properties"
})
@Import({
		JpaMoreTestJpaConfig.class,
		JpaMoreTestApplicationPropertyConfig.class
})
@ComponentScan(basePackageClasses = { JpaMoreTestBusinessPackage.class })
@EnableAspectJAutoProxy
public class JpaMoreTestConfig extends AbstractApplicationConfig {

	@Bean
	public static CoreConfigurer configurer() {
		return new CoreConfigurer();
	}
	
	@Bean
	public IRendererService rendererService() {
		return new EmptyRendererServiceImpl();
	}

}
