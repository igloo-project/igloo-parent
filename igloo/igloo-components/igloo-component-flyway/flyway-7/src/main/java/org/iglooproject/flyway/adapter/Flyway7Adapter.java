package org.iglooproject.flyway.adapter;

import org.flywaydb.core.api.configuration.Configuration;
import org.iglooproject.flyway.IFlywayAdapter;

public class Flyway7Adapter implements IFlywayAdapter {

	@Override
	public boolean isDetectEncoding(Configuration configuration) {
		return configuration.getDetectEncoding();
	}

	@Override
	public boolean isFailOnMissingLocations(Configuration configuration) {
		return configuration.getFailOnMissingLocations();
	}

}
