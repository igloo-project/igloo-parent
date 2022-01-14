package org.igloo.spring.autoconfigure.jpa;

import org.iglooproject.jpa.config.spring.JpaConfigUtils;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.business.search.query.HibernateSearchLuceneQueryFactoryImpl;
import org.iglooproject.jpa.more.business.search.query.IHibernateSearchLuceneQueryFactory;
import org.iglooproject.jpa.more.config.spring.JpaMoreApplicationPropertyRegistryConfig;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.jpa-more.disabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnBean({ IglooJpaAutoConfiguration.class })
@AutoConfigureAfter({ IglooJpaAutoConfiguration.class })
@Import({
	IglooJpaMoreComponentScanConfig.class,
	JpaMoreApplicationPropertyRegistryConfig.class
})
public class IglooJpaMoreAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(IJpaConfigurationProvider jpaConfigurationProvider) {
		return JpaConfigUtils.entityManagerFactory(jpaConfigurationProvider);
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
	@ConditionalOnMissingBean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IHibernateSearchLuceneQueryFactory hibernateSearchLuceneQueryFactory() {
		return new HibernateSearchLuceneQueryFactoryImpl();
	}

}