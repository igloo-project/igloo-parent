package org.iglooproject.flyway;

public interface IFlywayConfiguration {

	String getTable();

	String getSchemas();

	String getLocations();

}