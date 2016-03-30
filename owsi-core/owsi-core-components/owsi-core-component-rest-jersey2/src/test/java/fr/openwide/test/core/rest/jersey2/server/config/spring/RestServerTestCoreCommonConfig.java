package fr.openwide.test.core.rest.jersey2.server.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.openwide.core.spring.config.spring.AbstractApplicationConfig;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;
import fr.openwide.test.core.rest.jersey2.business.RestTestBusinessPackage;
import fr.openwide.test.core.rest.jersey2.server.RestServerPackage;

@Configuration
@ApplicationDescription(name = "rest-test-server")
@ConfigurationLocations(locations = {
		"classpath:owsi-core-component-jpa.properties",
		"classpath:configuration-private.properties",
		"classpath:rest-server.properties"
})
@Import({
	RestServerTestJpaConfig.class,
	RestServerTestApplicationPropertyConfig.class
})
@ComponentScan(
		basePackageClasses = { RestTestBusinessPackage.class, RestServerPackage.class },
		excludeFilters = @Filter(Configuration.class)
)
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class RestServerTestCoreCommonConfig extends AbstractApplicationConfig {
}