package fr.openwide.core.jpa.config.spring.provider;

import java.util.List;

import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.hibernate.cfg.NamingStrategy;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class DefaultJpaConfigurationProvider implements IJpaConfigurationProvider {

	@Autowired
	private List<JpaPackageScanProvider> jpaPackageScanProviders;

	@Value("${${db.type}.db.dialect}")
	private Class<Dialect> dialect;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2Ddl;

	@Value("${hibernate.hbm2ddl.import_files}")
	private String hbm2DdlImportFiles;

	@Value("${hibernate.defaultBatchSize}")
	private Integer defaultBatchSize;

	@Value("${lucene.index.path}")
	private String hibernateSearchIndexBase;
	
	@Value("${hibernate.search.indexing_strategy:}") // Defaults to an empty string
	private String hibernateSearchIndexingStrategy;

	@Value("#{dataSource}")
	private DataSource dataSource;

	@Value("${hibernate.ehCache.configurationLocation}")
	private String ehCacheConfiguration;

	@Value("${hibernate.ehCache.singleton}")
	private boolean ehCacheSingleton;

	@Value("${hibernate.queryCache.enabled}")
	private boolean queryCacheEnabled;

	@Autowired(required=false)
	private PersistenceProvider persistenceProvider;

	@Value("${javax.persistence.validation.mode}")
	private String validationMode;
	
	@Value("${hibernate.ejb.naming_strategy}")
	private Class<NamingStrategy> namingStrategy;

	@Value("${hibernate.create_empty_composites.enabled}")
	private boolean createEmptyCompositesEnabled;

	@Override
	public List<JpaPackageScanProvider> getJpaPackageScanProviders() {
		return jpaPackageScanProviders;
	}

	@Override
	public Class<Dialect> getDialect() {
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
		return hibernateSearchIndexingStrategy;
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
	public Class<NamingStrategy> getNamingStrategy() {
		return namingStrategy;
	}

	@Override
	public boolean isCreateEmptyCompositesEnabled() {
		return createEmptyCompositesEnabled;
	}

}
