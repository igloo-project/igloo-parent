package org.iglooproject.basicapp.core.config.spring;

import org.iglooproject.basicapp.core.BasicApplicationCorePackage;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ManifestPropertySource(prefix = "basic-application.core")
@Import({
	BasicApplicationCoreTaskManagementConfig.class,		// configuration de la gestion des tâches
	BasicApplicationCoreNotificationConfig.class,		// configuration des notifications
	BasicApplicationCoreSchedulingConfig.class,			// configuration des tâches planifiées
	BasicApplicationCoreApplicationPropertyConfig.class	// configuration des propriétés de l'application
})
@ComponentScan(
	basePackageClasses = {
		BasicApplicationCorePackage.class
	},
	// https://jira.springsource.org/browse/SPR-8808
	// on veut charger de manière explicite le contexte ; de ce fait,
	// on ignore l'annotation @Configuration sur le scan de package.
	excludeFilters = @Filter(Configuration.class)
)
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement(order = BasicApplicationAdviceOrder.TRANSACTION)
public class BasicApplicationCoreCommonConfig { //NOSONAR

	public static final String APPLICATION_NAME = "basic-application";

	public static final String PROFILE_TEST = "test";

}