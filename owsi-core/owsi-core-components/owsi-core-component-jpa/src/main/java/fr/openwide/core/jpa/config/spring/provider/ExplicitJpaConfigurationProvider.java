package fr.openwide.core.jpa.config.spring.provider;

import java.util.List;

import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.hibernate.cfg.NamingStrategy;
import org.hibernate.dialect.Dialect;

/**
 * @deprecated Présent pour compatibilité ascendante uniquement.
 * ATTENTION : en cas d'ajout de propriétés dans {@link IJpaConfigurationProvider}, ne pas rajouter
 * d'attributs dans cette classe, mais simplement renvoyer une valeur par défaut. Le but est
 * vraiment de faire disparaître cette classe à terme.
 */
@Deprecated
public class ExplicitJpaConfigurationProvider implements IJpaConfigurationProvider {
	private List<JpaPackageScanProvider> jpaPackageScanProviders;

	private Class<? extends Dialect> dialect;

	private String hbm2Ddl;

	private String hbm2DdlImportFiles;

	private Integer defaultBatchSize;

	private String hibernateSearchIndexBase;

	private DataSource dataSource;

	private String ehCacheConfiguration;

	private boolean ehCacheSingleton;

	private boolean queryCacheEnabled;

	private PersistenceProvider persistenceProvider;

	private String validationMode;
	
	private Class<? extends NamingStrategy> namingStrategy;

	private boolean createEmptyCompositesEnabled;

	public ExplicitJpaConfigurationProvider(Class<? extends Dialect> dialect, String hbm2Ddl, String hbm2DdlImportFiles, Integer defaultBatchSize,
			String hibernateSearchIndexBase, String ehCacheConfiguration,
			boolean ehCacheSingleton, boolean queryCacheEnabled,
			String validationMode, Class<NamingStrategy> namingStrategy) {
		this(
				null, dialect, hbm2Ddl, hbm2DdlImportFiles, defaultBatchSize,
				hibernateSearchIndexBase, null, ehCacheConfiguration, ehCacheSingleton, queryCacheEnabled,
				null, validationMode, namingStrategy,
				false
		);
	}

	public ExplicitJpaConfigurationProvider(Class<? extends Dialect> dialect, String hbm2Ddl, String hbm2DdlImportFiles, Integer defaultBatchSize,
			String hibernateSearchIndexBase, String ehCacheConfiguration,
			boolean ehCacheSingleton, boolean queryCacheEnabled,
			String validationMode, Class<NamingStrategy> namingStrategy, boolean createEmptyCompositesEnabled) {
		this(
				null, dialect, hbm2Ddl, hbm2DdlImportFiles, defaultBatchSize,
				hibernateSearchIndexBase, null, ehCacheConfiguration, ehCacheSingleton, queryCacheEnabled,
				null, validationMode, namingStrategy,
				createEmptyCompositesEnabled
		);
	}

	public ExplicitJpaConfigurationProvider(List<JpaPackageScanProvider> jpaPackageScanProviders,
			Class<? extends Dialect> dialect, String hbm2Ddl, String hbm2DdlImportFiles, Integer defaultBatchSize,
			String hibernateSearchIndexBase, DataSource dataSource, String ehCacheConfiguration,
			boolean ehCacheSingleton, boolean queryCacheEnabled, PersistenceProvider persistenceProvider,
			String validationMode, Class<NamingStrategy> namingStrategy) {
		this(
				jpaPackageScanProviders, dialect, hbm2Ddl, hbm2DdlImportFiles, defaultBatchSize,
				hibernateSearchIndexBase, dataSource, ehCacheConfiguration, ehCacheSingleton, queryCacheEnabled,
				persistenceProvider, validationMode, namingStrategy,
				false
		);
	}

	public ExplicitJpaConfigurationProvider(List<JpaPackageScanProvider> jpaPackageScanProviders,
			Class<? extends Dialect> dialect, String hbm2Ddl, String hbm2DdlImportFiles, Integer defaultBatchSize,
			String hibernateSearchIndexBase, DataSource dataSource, String ehCacheConfiguration,
			boolean ehCacheSingleton, boolean queryCacheEnabled, PersistenceProvider persistenceProvider,
			String validationMode, Class<NamingStrategy> namingStrategy, boolean createEmptyCompositesEnabled) {
		super();
		this.jpaPackageScanProviders = jpaPackageScanProviders;
		this.dialect = dialect;
		this.hbm2Ddl = hbm2Ddl;
		this.hbm2DdlImportFiles = hbm2DdlImportFiles;
		this.defaultBatchSize = defaultBatchSize;
		this.hibernateSearchIndexBase = hibernateSearchIndexBase;
		this.dataSource = dataSource;
		this.ehCacheConfiguration = ehCacheConfiguration;
		this.ehCacheSingleton = ehCacheSingleton;
		this.queryCacheEnabled = queryCacheEnabled;
		this.persistenceProvider = persistenceProvider;
		this.validationMode = validationMode;
		this.namingStrategy = namingStrategy;
		this.createEmptyCompositesEnabled = createEmptyCompositesEnabled;
	}

	@Override
	public List<JpaPackageScanProvider> getJpaPackageScanProviders() {
		return jpaPackageScanProviders;
	}

	@Override
	public Class<? extends Dialect> getDialect() {
		return dialect;
	}

	@Override
	public String getHbm2Ddl() {
		return hbm2Ddl;
	}

	@Override
	public String getHbm2DdlImportFiles() {
		return hbm2DdlImportFiles;
	}

	@Override
	public Integer getDefaultBatchSize() {
		return defaultBatchSize;
	}

	@Override
	public String getHibernateSearchIndexBase() {
		return hibernateSearchIndexBase;
	}
	
	@Override
	public String getHibernateSearchIndexingStrategy() {
		return null;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public String getEhCacheConfiguration() {
		return ehCacheConfiguration;
	}

	@Override
	public boolean isEhCacheSingleton() {
		return ehCacheSingleton;
	}

	@Override
	public boolean isQueryCacheEnabled() {
		return queryCacheEnabled;
	}

	@Override
	public PersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}

	@Override
	public String getValidationMode() {
		return validationMode;
	}

	@Override
	public Class<? extends NamingStrategy> getNamingStrategy() {
		return namingStrategy;
	}

	@Override
	public boolean isCreateEmptyCompositesEnabled() {
		return createEmptyCompositesEnabled;
	}
}