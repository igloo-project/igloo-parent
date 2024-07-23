package org.iglooproject.jpa.config.spring.provider;

import java.sql.Driver;

public interface IDatabaseConnectionConfigurationProvider {

  Class<Driver> getDriverClass();

  DatasourceProvider getProvider();
}
