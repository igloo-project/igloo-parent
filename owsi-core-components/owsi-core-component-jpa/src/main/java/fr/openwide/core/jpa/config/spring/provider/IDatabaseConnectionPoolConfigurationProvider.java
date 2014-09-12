package fr.openwide.core.jpa.config.spring.provider;

import java.sql.Driver;

public interface IDatabaseConnectionPoolConfigurationProvider {

	Class<Driver> getDriverClass();

	String getUrl();

	String getUser();

	String getPassword();

	int getMinIdle();

	int getMaxPoolSize();

	String getValidationQuery();

	String getInitSql();

}
