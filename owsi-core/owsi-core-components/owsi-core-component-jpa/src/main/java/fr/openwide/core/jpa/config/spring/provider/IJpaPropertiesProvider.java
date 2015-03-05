package fr.openwide.core.jpa.config.spring.provider;

import org.hibernate.cfg.NamingStrategy;
import org.hibernate.dialect.Dialect;

public interface IJpaPropertiesProvider {

	Class<? extends Dialect> getDialect();

	String getHbm2Ddl();

	String getHbm2DdlImportFiles();

	Integer getDefaultBatchSize();

	String getHibernateSearchIndexBase();

	String getHibernateSearchIndexingStrategy();

	String getEhCacheConfiguration();

	boolean isEhCacheSingleton();

	boolean isQueryCacheEnabled();

	String getValidationMode();

	Class<? extends NamingStrategy> getNamingStrategy();

	boolean isCreateEmptyCompositesEnabled();

}