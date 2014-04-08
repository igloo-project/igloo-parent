package fr.openwide.core.jpa.config.spring.provider;

import java.sql.Driver;

import org.springframework.beans.factory.annotation.Value;

public class DatabaseConnectionPoolConfigurationProvider {

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

	public Class<Driver> getDriverClass() {
		return driverClass;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

}
