package fr.openwide.core.jpa.config.spring.provider;

import java.sql.Driver;

import org.springframework.beans.factory.annotation.Value;

public class DatabaseConnectionPoolConfigurationProvider implements IDatabaseConnectionPoolConfigurationProvider {

	@Value("${${db.type}.db.driverClass}")
	private Class<Driver> driverClass;

	@Value("${db.jdbcUrl}")
	private String url;

	@Value("${db.user}")
	private String user;

	@Value("${db.password}")
	private String password;

	@Value("${db.minIdle}")
	private int minIdle;

	@Value("${db.maxPoolSize}")
	private int maxPoolSize;

	@Value("${${db.type}.db.preferredTestQuery}")
	private String validationQuery;

	@Value("${db.initSql}")
	private String initSql;

	@Override
	public Class<Driver> getDriverClass() {
		return driverClass;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public int getMinIdle() {
		return minIdle;
	}

	@Override
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	@Override
	public String getValidationQuery() {
		return validationQuery;
	}

	@Override
	public String getInitSql() {
		return initSql;
	}

}
