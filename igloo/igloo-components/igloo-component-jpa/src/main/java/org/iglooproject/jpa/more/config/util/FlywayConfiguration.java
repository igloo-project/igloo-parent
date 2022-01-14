package org.iglooproject.jpa.more.config.util;

import org.iglooproject.flyway.IFlywayConfiguration;
import org.springframework.beans.factory.annotation.Value;

public class FlywayConfiguration implements IFlywayConfiguration {

	@Value("${flyway.locations}")
	private String locations;
	
	@Value("${flyway.schemas}")
	private String schemas;
	
	@Value("${flyway.table}")
	private String table;

	@Override
	public String getLocations() {
		return locations;
	}

	@Override
	public String getSchemas() {
		return schemas;
	}

	@Override
	public String getTable() {
		return table;
	}
	
}
