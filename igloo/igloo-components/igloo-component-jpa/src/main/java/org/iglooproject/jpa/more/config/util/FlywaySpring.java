package org.iglooproject.jpa.more.config.util;

import org.flywaydb.core.Flyway;
import org.springframework.context.ConfigurableApplicationContext;

public class FlywaySpring extends Flyway {

	private ConfigurableApplicationContext applicationContext;

	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
