package org.iglooproject.test.jpa.externallinkchecker.business.rest.server.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.jpa.externallinkchecker.business.rest.server.RestServerPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This configuration relies on bootstrap-configuration.properties enforcing igloo.profile=test
 */
@Configuration
@PropertySource(name = IglooPropertySourcePriority.APPLICATION,
	value = {
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		"classpath:rest-server.properties"
	}
)
@Import({
	RestServerTestApplicationPropertyConfig.class
})
@ComponentScan(
		basePackageClasses = { RestServerPackage.class },
		excludeFilters = @Filter(Configuration.class)
)
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class RestServerTestCoreCommonConfig extends AbstractApplicationConfig {
}