package org.iglooproject.jpa.more.config.spring;

import org.apache.lucene.search.SortField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import org.iglooproject.jpa.config.spring.AbstractJpaConfig;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.more.business.generic.query.ISimpleGenericListItemSearchQuery;
import org.iglooproject.jpa.more.business.generic.query.SimpleGenericListItemHibernateSearchSearchQueryImpl;
import org.iglooproject.jpa.more.business.search.query.HibernateSearchLuceneQueryFactoryImpl;
import org.iglooproject.jpa.more.business.search.query.IHibernateSearchLuceneQueryFactory;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.task.search.IQueuedTaskHolderSearchQuery;
import org.iglooproject.jpa.more.business.task.search.QueuedTaskHolderSearchQueryImpl;
import org.iglooproject.jpa.more.util.CoreJpaMoreUtilPackage;

@Import({
	JpaMoreApplicationPropertyRegistryConfig.class,
	JpaMoreInfinispanConfig.class
})
@ComponentScan(basePackageClasses = { CoreJpaMoreBusinessPackage.class, CoreJpaMoreUtilPackage.class })
public abstract class AbstractJpaMoreJpaConfig extends AbstractJpaConfig {

	@Bean
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		return new TransactionTemplate(transactionManager);
	}

	@Bean
	public JpaPackageScanProvider jpaMorePackageScanProvider() {
		return new JpaPackageScanProvider(CoreJpaMoreBusinessPackage.class.getPackage());
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public <T extends GenericListItem<? super T>, S extends ISort<SortField>>
			ISimpleGenericListItemSearchQuery<T, S> simpleGenericListItemSearchQuery(Class<T> clazz) {
		return new SimpleGenericListItemHibernateSearchSearchQueryImpl<T, S>(clazz);
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IHibernateSearchLuceneQueryFactory hibernateSearchLuceneQueryFactory() {
		return new HibernateSearchLuceneQueryFactoryImpl();
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IQueuedTaskHolderSearchQuery queuedTaskHolderSearchQuery() {
		return new QueuedTaskHolderSearchQueryImpl();
	}

}
