package fr.openwide.core.jpa.config.spring.provider;

import java.util.List;

import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

public interface IJpaConfigurationProvider extends IJpaPropertiesProvider {

	List<JpaPackageScanProvider> getJpaPackageScanProviders();

	DataSource getDataSource();
	
	PersistenceProvider getPersistenceProvider();

}