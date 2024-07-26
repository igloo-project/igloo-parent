package org.iglooproject.jpa.config.spring.provider;

import igloo.hibernateconfig.api.IJpaPropertiesProvider;
import java.util.List;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

public interface IJpaConfigurationProvider extends IJpaPropertiesProvider {

  List<JpaPackageScanProvider> getJpaPackageScanProviders();

  DataSource getDataSource();

  PersistenceProvider getPersistenceProvider();
}
