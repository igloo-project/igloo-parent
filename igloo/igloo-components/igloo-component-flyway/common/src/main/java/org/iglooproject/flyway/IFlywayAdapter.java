package org.iglooproject.flyway;

import org.flywaydb.core.api.configuration.Configuration;

public interface IFlywayAdapter {

	boolean isDetectEncoding(Configuration configuration);
	boolean isFailOnMissingLocations(Configuration configuration);

}
