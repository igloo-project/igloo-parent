package fr.openwide.core.jpa.more.config.spring;

import org.apache.lucene.search.SortField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import fr.openwide.core.jpa.config.spring.AbstractJpaConfig;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.more.business.CoreJpaMoreBusinessPackage;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.query.ISimpleGenericListItemSearchQuery;
import fr.openwide.core.jpa.more.business.generic.query.SimpleGenericListItemHibernateSearchSearchQueryImpl;
import fr.openwide.core.jpa.more.business.search.query.HibernateSearchLuceneQueryFactoryImpl;
import fr.openwide.core.jpa.more.business.search.query.IHibernateSearchLuceneQueryFactory;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.task.search.IQueuedTaskHolderSearchQuery;
import fr.openwide.core.jpa.more.business.task.search.QueuedTaskHolderSearchQueryImpl;
import fr.openwide.core.jpa.more.util.CoreJpaMoreUtilPackage;

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
