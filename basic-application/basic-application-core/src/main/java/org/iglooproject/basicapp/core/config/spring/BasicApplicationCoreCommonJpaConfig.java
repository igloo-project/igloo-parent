package org.iglooproject.basicapp.core.config.spring;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.ISimpleLocalizedReferenceDataSearchQuery;
import org.iglooproject.basicapp.core.business.referencedata.search.SimpleLocalizedReferenceDataSearchQueryImpl;
import org.iglooproject.basicapp.core.config.hibernate.HibernateConfigPackage;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.security.config.spring.AbstractConfiguredJpaSecurityJpaConfig;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableAspectJAutoProxy
public class BasicApplicationCoreCommonJpaConfig extends AbstractConfiguredJpaSecurityJpaConfig {

	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Override
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(
				BasicApplicationCoreCommonBusinessPackage.class.getPackage(),
				HibernateConfigPackage.class.getPackage() // Typedef config
		);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public <T extends LocalizedReferenceData<? super T>, S extends ISort<SortField>> ISimpleLocalizedReferenceDataSearchQuery<T, S> simpleLocalizedReferenceDataSearchQuery(Class<T> clazz) {
		return new SimpleLocalizedReferenceDataSearchQueryImpl<T, S>(clazz);
	}

}
