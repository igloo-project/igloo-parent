package org.iglooproject.basicapp.core.config.spring;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.BasicApplicationCorePackage;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSearchQueryImpl;
import org.iglooproject.basicapp.core.business.referencedata.search.IBasicReferenceDataSearchQuery;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ManifestPropertySource(prefix = "basic-application.core")
@Import({
	BasicApplicationJpaModelConfiguration.class,
	BasicApplicationCoreSecurityConfig.class,
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
public class BasicApplicationCoreCommonConfiguration { //NOSONAR

	public static final String APPLICATION_NAME = "basic-application";

	public static final String PROFILE_TEST = "test";

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public <T extends ReferenceData<? super T>, S extends ISort<SortField>> IBasicReferenceDataSearchQuery<T, S> basicReferenceDataSearchQuery(Class<T> clazz) {
		return new BasicReferenceDataSearchQueryImpl<>(clazz);
	}

	@Configuration
	@ComponentScan(basePackages = "db.migration.init")
	@ConditionalOnProperty(name = "migration.init.enabled", havingValue = "true", matchIfMissing = true)
	public class JavaMigration {
		
	}

}