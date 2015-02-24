package fr.openwide.core.jpa.config.spring;

import static com.impossibl.postgres.utils.guava.Strings.emptyToNull;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.SharedCacheMode;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cfg.EJB3NamingStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.jpa.AvailableSettings;
import org.hibernate.loader.BatchFetchStyle;
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
import com.zaxxer.hikari.HikariDataSource;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.config.spring.provider.ExplicitJpaConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.IDatabaseConnectionPoolConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.IJpaConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.IJpaPropertiesProvider;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.hibernate.cache.ehcache.EhCache285RegionFactory;
import fr.openwide.core.jpa.hibernate.cache.ehcache.SingletonEhCache285RegionFactory;
import fr.openwide.core.jpa.util.FixedDefaultComponentSafeNamingStrategy;

public final class JpaConfigUtils {

	private JpaConfigUtils() {}

	/**
	 * Construit un {@link LocalContainerEntityManagerFactoryBean} à partir d'un ensemble d'options usuelles.
	 */
	public static LocalContainerEntityManagerFactoryBean entityManagerFactory(IJpaConfigurationProvider provider) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setJpaProperties(getJpaProperties(provider));
		entityManagerFactoryBean.setDataSource(provider.getDataSource());
		entityManagerFactoryBean.setPackagesToScan(getPackagesToScan(provider.getJpaPackageScanProviders()));
		
		PersistenceProvider persistenceProvider = provider.getPersistenceProvider();
		if (persistenceProvider != null) {
			entityManagerFactoryBean.setPersistenceProvider(persistenceProvider);
		}
		
		return entityManagerFactoryBean;
	}

	@Deprecated
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
			String validationMode,
			Class<NamingStrategy> namingStrategy) {
		return entityManagerFactory(new ExplicitJpaConfigurationProvider(
				jpaPackageScanProviders, dialect, hibernateHbm2Ddl, hibernateHbm2DdlImportFiles, defaultBatchSize,
				hibernateSearchIndexBase, dataSource, ehCacheConfiguration, singletonCache, queryCacheEnabled,
				persistenceProvider, validationMode, namingStrategy));
	}

	/**
	 * Construit un {@link LocalContainerEntityManagerFactoryBean} à partir d'un ensemble d'options usuelles.
	 * @deprecated Utiliser {@link #entityManagerFactory(IJpaConfigurationProvider)}
	 */
	@Deprecated
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
			String validationMode,
			Class<NamingStrategy> namingStrategy,
			boolean createEmptyCompositesEnabled) {
		return entityManagerFactory(new ExplicitJpaConfigurationProvider(
				jpaPackageScanProviders, dialect, hibernateHbm2Ddl, hibernateHbm2DdlImportFiles, defaultBatchSize,
				hibernateSearchIndexBase, dataSource, ehCacheConfiguration, singletonCache, queryCacheEnabled,
				persistenceProvider, validationMode, namingStrategy, createEmptyCompositesEnabled));
	}

	/**
	 * @deprecated Utiliser {@link #getJpaProperties(IJpaPropertiesProvider)}
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static Properties getJpaProperties(Class<?> dialect,
			String hibernateHbm2Ddl,
			String hibernateHbm2DdlImportFiles,
			String hibernateSearchIndexBase,
			String ehCacheConfiguration,
			boolean singletonCache,
			boolean queryCacheEnabled,
			Integer defaultBatchSize,
			String validationMode,
			Class<NamingStrategy> namingStrategy) {
		return getJpaProperties(new ExplicitJpaConfigurationProvider(
				(Class<? extends Dialect>)dialect, hibernateHbm2Ddl, hibernateHbm2DdlImportFiles, defaultBatchSize, hibernateSearchIndexBase, 
				ehCacheConfiguration, singletonCache, queryCacheEnabled, validationMode, namingStrategy
		));
	}
	
	/**
	 * @deprecated Utiliser {@link #getJpaProperties(IJpaPropertiesProvider)}
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static Properties getJpaProperties(Class<?> dialect,
			String hibernateHbm2Ddl,
			String hibernateHbm2DdlImportFiles,
			String hibernateSearchIndexBase,
			String ehCacheConfiguration,
			boolean singletonCache,
			boolean queryCacheEnabled,
			Integer defaultBatchSize,
			String validationMode,
			Class<NamingStrategy> namingStrategy,
			boolean createEmptyCompositesEnabled) {
		return getJpaProperties(new ExplicitJpaConfigurationProvider(
				(Class<? extends Dialect>)dialect, hibernateHbm2Ddl, hibernateHbm2DdlImportFiles, defaultBatchSize, hibernateSearchIndexBase, 
				ehCacheConfiguration, singletonCache, queryCacheEnabled, validationMode, namingStrategy,
				createEmptyCompositesEnabled
		));
	}
	
	public static Properties getJpaProperties(IJpaPropertiesProvider configuration) {
		Properties properties = new Properties();
		properties.setProperty(Environment.DIALECT, configuration.getDialect().getName());
		properties.setProperty(Environment.HBM2DDL_AUTO, configuration.getHbm2Ddl());
		properties.setProperty(Environment.SHOW_SQL, Boolean.FALSE.toString());
		properties.setProperty(Environment.FORMAT_SQL, Boolean.FALSE.toString());
		properties.setProperty(Environment.GENERATE_STATISTICS, Boolean.FALSE.toString());
		properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, Boolean.TRUE.toString());
		properties.setProperty(org.hibernate.cfg.AvailableSettings.CREATE_EMPTY_COMPOSITES_ENABLED,
				Boolean.valueOf(configuration.isCreateEmptyCompositesEnabled()).toString());
		Integer defaultBatchSize = configuration.getDefaultBatchSize();
		if (defaultBatchSize != null) {
			properties.setProperty(Environment.DEFAULT_BATCH_FETCH_SIZE, Integer.toString(defaultBatchSize));
			properties.setProperty(Environment.BATCH_FETCH_STYLE, BatchFetchStyle.PADDED.name());
		}
		
		String hibernateHbm2DdlImportFiles = configuration.getHbm2DdlImportFiles();
		if (StringUtils.hasText(hibernateHbm2DdlImportFiles)) {
			properties.setProperty(Environment.HBM2DDL_IMPORT_FILES, hibernateHbm2DdlImportFiles);
		}

		String ehCacheConfiguration = configuration.getEhCacheConfiguration();
		boolean singletonCache = configuration.isEhCacheSingleton();
		boolean queryCacheEnabled = configuration.isQueryCacheEnabled();
		if (StringUtils.hasText(ehCacheConfiguration)) {
			if (singletonCache) {
				properties.setProperty(Environment.CACHE_REGION_FACTORY, SingletonEhCache285RegionFactory.class.getName());
			} else {
				properties.setProperty(Environment.CACHE_REGION_FACTORY, EhCache285RegionFactory.class.getName());
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

		String hibernateSearchIndexBase = configuration.getHibernateSearchIndexBase();
		if (StringUtils.hasText(hibernateSearchIndexBase)) {
			properties.setProperty("hibernate.search.default.directory_provider", FSDirectoryProvider.class.getName());
			properties.setProperty("hibernate.search.default.indexBase", hibernateSearchIndexBase);
			properties.setProperty("hibernate.search.default.exclusive_index_use", Boolean.TRUE.toString());
			properties.setProperty("hibernate.search.default.locking_strategy", "native");
			properties.setProperty(org.hibernate.search.Environment.LUCENE_MATCH_VERSION,
					org.hibernate.search.Environment.DEFAULT_LUCENE_MATCH_VERSION.name());
		} else {
			properties.setProperty("hibernate.search.autoregister_listeners", Boolean.FALSE.toString());
		}
		
		String hibernateSearchIndexingStrategy = configuration.getHibernateSearchIndexingStrategy();
		if (StringUtils.hasText(hibernateSearchIndexingStrategy)) {
			properties.setProperty(org.hibernate.search.Environment.INDEXING_STRATEGY, hibernateSearchIndexingStrategy);
		}

		String validationMode = configuration.getValidationMode();
		if (StringUtils.hasText(validationMode)) {
			properties.setProperty(AvailableSettings.VALIDATION_MODE, validationMode);
		}
		
		Class<? extends NamingStrategy> namingStrategy = configuration.getNamingStrategy();
		if (namingStrategy != null) {
			properties.setProperty(AvailableSettings.NAMING_STRATEGY, namingStrategy.getName());
		} else {
			throw new IllegalStateException(AvailableSettings.NAMING_STRATEGY + " may not be null: sensible values are "
					+ EJB3NamingStrategy.class.getName() + " for OWSI-Core <= 0.7 or "
					+ FixedDefaultComponentSafeNamingStrategy.class.getName() + " for OWSI-Core >= 0.8");
		}
		
		return properties;
	}

	public static Advisor defaultTransactionAdvisor(PlatformTransactionManager transactionManager) {
		return defaultTransactionAdvisor(transactionManager, Lists.<Class<? extends Exception>>newArrayList());
	}

	public static Advisor defaultTransactionAdvisor(PlatformTransactionManager transactionManager,
			List<Class<? extends Exception>> additionalRollbackRuleExceptions) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		
		advisor.setExpression("this(" + ITransactionalAspectAwareService.class.getName() + ")");
		advisor.setAdvice(defaultTransactionInterceptor(transactionManager, additionalRollbackRuleExceptions));
		
		return advisor;
		
	}

	/**
	 * Construit un transactionInterceptor avec une configuration par défaut.
	 */
	public static TransactionInterceptor defaultTransactionInterceptor(PlatformTransactionManager transactionManager,
			List<Class<? extends Exception>> additionalRollbackRuleExceptions) {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		Properties transactionAttributes = new Properties();
		
		List<RollbackRuleAttribute> rollbackRules = Lists.newArrayList();
		rollbackRules.add(new RollbackRuleAttribute(ServiceException.class));
		// TODO voir si on ajoute SecurityServiceException.class en fonction de ce que ça donne sur le Wombat
		// ou voir si on ne la dégage pas carrément en fait...
		
		for (Class<? extends Exception> clazz : additionalRollbackRuleExceptions) {
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
		transactionAttributes.setProperty("count*", readOnlyTransactionAttributesDefinition);
		// write et rollback-rule
		transactionAttributes.setProperty("*", writeTransactionAttributesDefinition);
		
		transactionInterceptor.setTransactionAttributes(transactionAttributes);
		transactionInterceptor.setTransactionManager(transactionManager);
		return transactionInterceptor;
	}

	public static DataSource dataSource(IDatabaseConnectionPoolConfigurationProvider configurationProvider) {
		HikariDataSource dataSource = new HikariDataSource();
		
		dataSource.setDriverClassName(configurationProvider.getDriverClass().getName());
		dataSource.setJdbcUrl(configurationProvider.getUrl());
		dataSource.setUsername(configurationProvider.getUser());
		dataSource.setPassword(configurationProvider.getPassword());
		dataSource.addDataSourceProperty("user", configurationProvider.getUser());
		dataSource.addDataSourceProperty("password", configurationProvider.getPassword());
		dataSource.setMinimumIdle(configurationProvider.getMinIdle());
		dataSource.setMaximumPoolSize(configurationProvider.getMaxPoolSize());
		dataSource.setConnectionTestQuery(configurationProvider.getValidationQuery());
		dataSource.setConnectionInitSql(emptyToNull(configurationProvider.getInitSql()));
		
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
