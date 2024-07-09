package org.iglooproject.basicapp.core.config.spring;

import org.iglooproject.basicapp.core.BasicApplicationCorePackage;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration(exclude = {
	FreeMarkerAutoConfiguration.class, // Not used and trigger a warning at startup
	// trigger warnings at startup
	// ... Is this bean getting eagerly injected into a currently created BeanPostProcessor
	// [projectingArgumentResolverBeanPostProcessor] ...
	// https://github.com/spring-projects/spring-data-jpa/issues/3244
	// https://github.com/spring-projects/spring-batch/issues/4489
	// https://github.com/spring-projects/spring-boot/issues/38558
	// https://github.com/spring-projects/spring-security/issues/14209
	// https://github.com/spring-projects/spring-ws/issues/1391
	SpringDataWebAutoConfiguration.class
})
@ManifestPropertySource(prefix = "basic-application.core")
@Import({
	BasicApplicationJpaModelConfiguration.class,
	BasicApplicationCoreSecurityConfig.class,
	BasicApplicationCoreTaskManagementConfig.class,			// configuration de la gestion des tâches
	BasicApplicationCoreNotificationConfig.class,			// configuration des notifications
	BasicApplicationCoreSchedulingConfig.class,				// configuration des tâches planifiées
	BasicApplicationCoreApplicationPropertyConfig.class,	// configuration des propriétés de l'application
	BasicApplicationReferenceDataConfiguration.class
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
public class BasicApplicationCoreCommonConfiguration { //NOSONAR

	public static final String APPLICATION_NAME = "basic-application";

	public static final String PROFILE_TEST = "test";

	@Configuration
	@ComponentScan(basePackages = "db.migration.init")
	@ConditionalOnProperty(name = "migration.init.enabled", havingValue = "true", matchIfMissing = true)
	public class JavaMigration {
		
	}

}