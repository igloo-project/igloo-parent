package fr.openwide.core.jpa.config.spring.provider;

import java.sql.Driver;

public class SimpleDatabaseConnectionPoolConfigurationProvider implements IDatabaseConnectionPoolConfigurationProvider {

	private Class<Driver> driverClass;

	private String url;

	private String user;

	private String password;

	private int minIdle;

	private int maxPoolSize;

	private String validationQuery;

	@Override
	public Class<Driver> getDriverClass() {
		return driverClass;
	}

	public SimpleDatabaseConnectionPoolConfigurationProvider setDriverClass(Class<Driver> driverClass) {
		this.driverClass = driverClass;
		return this;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public SimpleDatabaseConnectionPoolConfigurationProvider setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public String getUser() {
		return user;
	}

	public SimpleDatabaseConnectionPoolConfigurationProvider setUser(String user) {
		this.user = user;
		return this;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public SimpleDatabaseConnectionPoolConfigurationProvider setPassword(String password) {
		this.password = password;
		return this;
	}

	@Override
	public int getMinIdle() {
		return minIdle;
	}

	public SimpleDatabaseConnectionPoolConfigurationProvider setMinIdle(int minIdle) {
		this.minIdle = minIdle;
		return this;
	}

	@Override
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public SimpleDatabaseConnectionPoolConfigurationProvider setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}

	@Override
	public String getValidationQuery() {
		return validationQuery;
	}

	public SimpleDatabaseConnectionPoolConfigurationProvider setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
		return this;
	}

}
