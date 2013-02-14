package fr.openwide.core.jpa.config.spring;

import java.sql.Driver;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.SharedCacheMode;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.apache.lucene.util.Version;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.search.store.impl.FSDirectoryProvider;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.config.spring.provider.DefaultJpaConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.DefaultTomcatPoolConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public final class JpaConfigUtils {

	private JpaConfigUtils() {}

	public static LocalContainerEntityManagerFactoryBean entityManagerFactory(DefaultJpaConfigurationProvider provider) {
		return entityManagerFactory(
				provider.getJpaPackageScanProviders(),
				provider.getDialect(), provider.getHbm2Ddl(), provider.getHbm2DdlImportFiles(),
				provider.getHibernateSearchIndexBase(), provider.getDataSource(), 
				provider.getEhCacheConfiguration(), provider.isEhCacheSingleton(), provider.isQueryCacheEnabled(),
				provider.getDefaultBatchSize(), provider.getPersistenceProvider(), provider.getValidationMode());
	}

	/**
	 * Construit un {@link LocalContainerEntityManagerFactoryBean} à partir d'un ensemble d'options usuelles.
	 */
	public static LocalContainerEntityManagerFactoryBean entityManagerFactory(
			List<JpaPackageScanProvider> jpaPackageScanProviders,
			Class<Dialect> dialect,
			String hibernateHbm2Ddl,
			String hibernateHbm2DdlImportFiles,
			String hibernateSearchIndexBase,
			DataSource dataSource,
			String ehCacheConfiguration,
			boolean singletonCache,
			boolean queryCacheEnabled,
			Integer defaultBatchSize,
			PersistenceProvider persistenceProvider,
			String validationMode) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setJpaProperties(getJpaProperties(dialect, hibernateHbm2Ddl,
				hibernateHbm2DdlImportFiles, hibernateSearchIndexBase,
				ehCacheConfiguration, singletonCache, queryCacheEnabled, defaultBatchSize, validationMode));
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPackagesToScan(getPackagesToScan(jpaPackageScanProviders));
		
		if (persistenceProvider != null) {
			entityManagerFactoryBean.setPersistenceProvider(persistenceProvider);
		}
		
		return entityManagerFactoryBean;
	}

	public static Properties getJpaProperties(Class<?> dialect,
			String hibernateHbm2Ddl,
			String hibernateHbm2DdlImportFiles,
			String hibernateSearchIndexBase,
			String ehCacheConfiguration,
			boolean singletonCache,
			boolean queryCacheEnabled,
			Integer defaultBatchSize,
			String validationMode) {
		Properties properties = new Properties();
		properties.setProperty(Environment.DIALECT, dialect.getName());
		properties.setProperty(Environment.HBM2DDL_AUTO, hibernateHbm2Ddl);
		properties.setProperty(Environment.SHOW_SQL, Boolean.FALSE.toString());
		properties.setProperty(Environment.FORMAT_SQL, Boolean.FALSE.toString());
		properties.setProperty(Environment.GENERATE_STATISTICS, Boolean.TRUE.toString());
		properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, Boolean.TRUE.toString());
		if (defaultBatchSize != null) {
			properties.setProperty(Environment.DEFAULT_BATCH_FETCH_SIZE, Integer.toString(defaultBatchSize));
		}
		
		if (StringUtils.hasText(hibernateHbm2DdlImportFiles)) {
			properties.setProperty(Environment.HBM2DDL_IMPORT_FILES, hibernateHbm2DdlImportFiles);
		}
		
		if (StringUtils.hasText(ehCacheConfiguration)) {
			if (singletonCache) {
				properties.setProperty(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
			} else {
				properties.setProperty(Environment.CACHE_REGION_FACTORY, EhCacheRegionFactory.class.getName());
			}
			properties.setProperty(AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.ENABLE_SELECTIVE.name());
			properties.setProperty(EhCacheRegionFactory.NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME, ehCacheConfiguration);
			properties.setProperty(Environment.USE_SECOND_LEVEL_CACHE, Boolean.TRUE.toString());
			if (queryCacheEnabled) {
				properties.setProperty(Environment.USE_QUERY_CACHE, Boolean.TRUE.toString());
			} else {
				properties.setProperty(Environment.USE_QUERY_CACHE, Boolean.FALSE.toString());
			}
		} else {
			if (queryCacheEnabled) {
				throw new IllegalArgumentException("Could not enable query cache without EhCache configuration");
			}
			properties.setProperty(Environment.USE_SECOND_LEVEL_CACHE, Boolean.FALSE.toString());
			properties.setProperty(Environment.USE_QUERY_CACHE, Boolean.FALSE.toString());
		}
		
		if (StringUtils.hasText(hibernateSearchIndexBase)) {
			properties.setProperty("hibernate.search.default.directory_provider", FSDirectoryProvider.class.getName());
			properties.setProperty("hibernate.search.default.indexBase", hibernateSearchIndexBase);
			properties.setProperty("hibernate.search.default.exclusive_index_use", Boolean.TRUE.toString());
			properties.setProperty("hibernate.search.default.locking_strategy", "native");
			properties.setProperty(org.hibernate.search.Environment.LUCENE_MATCH_VERSION, Version.LUCENE_35.name());
		} else {
			properties.setProperty("hibernate.search.autoregister_listeners", Boolean.FALSE.toString());
		}
		
		if (StringUtils.hasText(validationMode)) {
			properties.setProperty(AvailableSettings.VALIDATION_MODE, validationMode);
		}
		
		return properties;
	}

	public static Advisor defaultTransactionAdvisor(PlatformTransactionManager transactionManager) {
		return defaultTransactionAdvisor(transactionManager, Lists.<Class<? extends Exception>>newArrayList());
	}

	public static Advisor defaultTransactionAdvisor(PlatformTransactionManager transactionManager, List<Class<? extends Exception>> exceptions) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		
		advisor.setExpression("this(" + ITransactionalAspectAwareService.class.getName() + ")");
		advisor.setAdvice(defaultTransactionInterceptor(transactionManager, exceptions));
		
		return advisor;
		
	}

	/**
	 * Construit un transactionInterceptor avec une configuration par défaut.
	 */
	public static TransactionInterceptor defaultTransactionInterceptor(PlatformTransactionManager transactionManager, List<Class<? extends Exception>> exceptions) {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		Properties transactionAttributes = new Properties();
		
		List<RollbackRuleAttribute> rollbackRules = Lists.newArrayList();
		rollbackRules.add(new RollbackRuleAttribute(ServiceException.class));
		rollbackRules.add(new RollbackRuleAttribute(SecurityServiceException.class));
		for (Class<? extends Exception> clazz : exceptions) {
			rollbackRules.add(new RollbackRuleAttribute(clazz));
		}
		
		DefaultTransactionAttribute readOnlyTransactionAttributes =
				new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		readOnlyTransactionAttributes.setReadOnly(true);
		
		RuleBasedTransactionAttribute writeTransactionAttributes =
				new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		
		String readOnlyTransactionAttributesDefinition = readOnlyTransactionAttributes.toString();
		String writeTransactionAttributesDefinition = writeTransactionAttributes.toString();
		// read-only
		transactionAttributes.setProperty("is*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("has*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("search*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("find*", readOnlyTransactionAttributesDefinition);
		// write et rollback-rule
		transactionAttributes.setProperty("*", writeTransactionAttributesDefinition);
		
		transactionInterceptor.setTransactionAttributes(transactionAttributes);
		transactionInterceptor.setTransactionManager(transactionManager);
		return transactionInterceptor;
	}

	public static DataSource dataSource(DefaultTomcatPoolConfigurationProvider provider) {
		return dataSource(provider.getDriver(), provider.getUrl(), provider.getUser(), provider.getPassword(),
				provider.getMinPoolSize(), provider.getMaxPoolSize(), provider.getInitialPoolSize(),
				provider.getValidationQuery());
	}

	public static DataSource dataSource(Class<? extends Driver> driver, String url, String username, String password,
			int minPoolSize, int maxPoolSize, int initialPoolSize, String validationQuery) {
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		dataSource.setDriverClassName(driver.getName());
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaxActive(maxPoolSize);
		dataSource.setMinIdle(minPoolSize);
		dataSource.setMaxIdle(maxPoolSize);
		dataSource.setInitialSize(initialPoolSize);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestOnBorrow(true);
		dataSource.setLogValidationErrors(false);
		dataSource.setValidationInterval(30000);
		
		return dataSource;
	}

	private static String[] getPackagesToScan(List<JpaPackageScanProvider> jpaPackageScanProviders) {
		Set<String> packagesToScan = new HashSet<String>();
		for (JpaPackageScanProvider jpaPackageScanProvider : jpaPackageScanProviders) {
			for (Package packageToScan : jpaPackageScanProvider.getPackages()) {
				packagesToScan.add(packageToScan.getName());
			}
		}
		return packagesToScan.toArray(new String[packagesToScan.size()]);
	}

}
