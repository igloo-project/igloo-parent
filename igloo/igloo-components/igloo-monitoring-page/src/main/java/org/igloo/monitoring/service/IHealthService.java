package org.igloo.monitoring.service;

public interface IHealthService {

	/**
	 * Lookup the current application status for a named metric.
	 * 
	 * @return a {@link HealthLookup} object. If any incident is encountered during lookup, a {@link HealthStatus#UNKNOWN}
	 * status is returned.
	 * @throws NoSuchElementException if metric is not known.
	 */
	HealthLookup getHealthLookup(String metric);

	/**
	 * Use this method to add new services with the {@code put} method. Use only during configuration. Not thread-safe.
	 * 
	 * @return this, to chain calls.
	 */
	HealthService addService(String name, IHealthMetricService service);

	/**
	 * Lookup a service by name.
	 * 
	 * @param name the name the service was registered with.
	 * @return a {@link IHealthMetricService}
	 * @throws NoSuchElementException if metric is not known.
	 */
	IHealthMetricService getService(String name);

}