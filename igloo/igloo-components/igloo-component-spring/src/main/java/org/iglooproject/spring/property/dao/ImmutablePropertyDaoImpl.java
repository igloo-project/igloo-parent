package org.iglooproject.spring.property.dao;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class ImmutablePropertyDaoImpl implements IImmutablePropertyDao, EnvironmentAware {

	/**
	 * Beware that Environment may or may not be a spring bean. We prefer {@link EnvironmentAware} interface usage.
	 */
	private Environment environment;

	@Override
	public String get(String key) {
		return environment.getProperty(key);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
