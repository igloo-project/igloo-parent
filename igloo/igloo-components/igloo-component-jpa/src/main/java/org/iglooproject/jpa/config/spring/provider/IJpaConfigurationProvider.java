package org.iglooproject.jpa.config.spring.provider;

import java.util.List;

import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import igloo.hibernateconfig.api.IJpaPropertiesProvider;

public interface IJpaConfigurationProvider extends IJpaPropertiesProvider {

	List<JpaPackageScanProvider> getJpaPackageScanProviders();

	DataSource getDataSource();
	
	PersistenceProvider getPersistenceProvider();

}