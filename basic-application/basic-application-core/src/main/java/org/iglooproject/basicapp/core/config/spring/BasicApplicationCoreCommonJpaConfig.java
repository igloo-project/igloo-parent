package org.iglooproject.basicapp.core.config.spring;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.search.BasicReferenceDataSearchQueryImpl;
import org.iglooproject.basicapp.core.business.referencedata.search.IBasicReferenceDataSearchQuery;
import org.iglooproject.basicapp.core.config.hibernate.HibernateConfigPackage;
import org.iglooproject.jpa.config.spring.JpaConfigUtils;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.business.search.query.HibernateSearchLuceneQueryFactoryImpl;
import org.iglooproject.jpa.more.business.search.query.IHibernateSearchLuceneQueryFactory;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.task.search.IQueuedTaskHolderSearchQuery;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSearchQueryImpl;
import org.iglooproject.jpa.more.config.spring.JpaMoreApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.more.config.spring.JpaMoreInfinispanConfig;
import org.iglooproject.jpa.more.util.CoreJpaMoreUtilPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Import({
	JpaMoreApplicationPropertyRegistryConfig.class,
	JpaMoreInfinispanConfig.class
})
@ComponentScan(basePackageClasses = { CoreJpaMoreBusinessPackage.class, CoreJpaMoreUtilPackage.class })
@Configuration
@EnableAspectJAutoProxy
public class BasicApplicationCoreCommonJpaConfig {
	
	@Autowired
	protected IJpaConfigurationProvider jpaConfigurationProvider;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		return JpaConfigUtils.entityManagerFactory(jpaConfigurationProvider);
	}

	/**
	 * Déclaration des packages de scan pour l'application.
	 */
	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(
			BasicApplicationCoreCommonBusinessPackage.class.getPackage(),
			HibernateConfigPackage.class.getPackage() // Typedef config
		);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public <T extends ReferenceData<? super T>, S extends ISort<SortField>> IBasicReferenceDataSearchQuery<T, S> basicReferenceDataSearchQuery(Class<T> clazz) {
		return new BasicReferenceDataSearchQueryImpl<>(clazz);
	}

	@Bean
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		return new TransactionTemplate(transactionManager);
	}

	@Bean
	public JpaPackageScanProvider jpaMorePackageScanProvider() {
		return new JpaPackageScanProvider(CoreJpaMoreBusinessPackage.class.getPackage());
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IHibernateSearchLuceneQueryFactory hibernateSearchLuceneQueryFactory() {
		return new HibernateSearchLuceneQueryFactoryImpl();
	}
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IQueuedTaskHolderSearchQuery queuedTaskHolderSearchQuery() {
		return new QueuedTaskHolderSearchQueryImpl();
	}

}
