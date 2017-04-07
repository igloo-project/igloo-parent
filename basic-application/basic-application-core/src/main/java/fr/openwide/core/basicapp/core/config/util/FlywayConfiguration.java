package fr.openwide.core.basicapp.core.config.util;

import org.springframework.beans.factory.annotation.Value;

public class FlywayConfiguration {

	@Value("${flyway.locations}")
	private String locations;
	
	@Value("${flyway.schemas}")
	private String schemas;
	
	@Value("${flyway.table}")
	private String table;

	public String getLocations() {
		return locations;
	}

	public String getSchemas() {
		return schemas;
	}

	public String getTable() {
		return table;
	}
	
}
