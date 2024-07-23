package org.igloo.monitoring.service;

public interface IHealthMetricService {

  /**
   * Lookup the current application status.
   *
   * @return a {@link HealthLookup} object. If any incident is encountered during lookup, a {@link
   *     HealthStatus#UNKNOWN} status is returned.
   */
  HealthLookup getHealthLookup();

  /**
   * Does this health service supports perfData ?
   *
   * @return true if this health service provides perfdata.
   */
  boolean supportsPerfData();
}
