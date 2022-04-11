package org.igloo.monitoring.perfdata;

public interface IHealthService {

	/**
	 * Lookup the current application status.
	 * 
	 * @return a {@link HealthLookup} object. If any incident is encountered during lookup, a {@link HealthStatus#UNKNOWN}
	 * status is returned.
	 */
	HealthLookup getHealthLookup();

	/**
	 * Does this health service supports perfData ?
	 * 
	 * @return true if this health service provides perfdata.
	 */
	boolean supportsPerfData();

}
