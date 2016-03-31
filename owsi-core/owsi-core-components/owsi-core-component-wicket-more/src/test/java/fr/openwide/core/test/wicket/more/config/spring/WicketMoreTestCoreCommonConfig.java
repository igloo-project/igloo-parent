package fr.openwide.core.test.wicket.more.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.openwide.core.spring.config.spring.AbstractApplicationConfig;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;
import fr.openwide.core.test.wicket.more.business.WicketMoreTestBusinessPackage;

@Configuration
@ApplicationDescription(name = "wicket-more-test")
@ConfigurationLocations(locations = {
		"classpath:owsi-core-component-jpa.properties",
		"classpath:configuration-private.properties",
		"classpath:owsi-hibernate.properties"
})
@Import({
	WicketMoreTestJpaConfig.class,
	WicketMoreTestApplicationPropertyConfig.class
})
@ComponentScan(
		basePackageClasses = WicketMoreTestBusinessPackage.class,
		excludeFilters = @Filter(Configuration.class)
)
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class WicketMoreTestCoreCommonConfig extends AbstractApplicationConfig {
}