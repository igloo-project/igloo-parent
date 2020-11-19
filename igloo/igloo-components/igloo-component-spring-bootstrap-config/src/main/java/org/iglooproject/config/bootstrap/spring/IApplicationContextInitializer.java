package org.iglooproject.config.bootstrap.spring;

public interface IApplicationContextInitializer {

	public static final String IGLOO_CONFIGURATION_LOGGER_NAME = "igloo@config";

	/**
	 * Log4j configurations to aggregate. This must be defined in bootstrap configuration.
	 */
	public static final String LOG4J_CONFIGURATIONS_PROPERTY = "igloo.log4j.configurationLocations";

	/**
	 * Log4j2 configurations to aggregate. This must be defined in bootstrap configuration.
	 */
	public static final String LOG4J2_CONFIGURATIONS_PROPERTY = "igloo.log4j2.configurationLocations";

	/**
	 * Igloo profile to load. This must be defined in bootstrap configuration.
	 */
	public static final String IGLOO_PROFILE_PROPERTY = "igloo.profile";

	/**
	 * Spring configurations to load. This must be defined in bootstrap configuration.
	 */
	public static final String IGLOO_PROFILES_LOCATIONS_PROPERTY = "igloo.configurationLocations";

	/**
	 * Igloo application name; used to resolve some placeholders, especially in configuration locations. This must be
	 * configured by an {@link ApplicationDescription} annotation.
	 */
	public static final String IGLOO_APPLICATION_NAME_PROPERTY = "igloo.applicationName";

	/**
	 * System property to use to specify replacing or added bootstrap configurations.
	 * 
	 * @see #BOOTSTRAP_OVERRIDE_DEFAULT_SYSTEM_PROPERTY
	 */
	public static final String BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY = "igloo.bootstrapLocations";

	/**
	 * Environment variable to use to specify replacing or added bootstrap configurations.
	 * 
	 * @see #BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT
	 */
	public static final String BOOTSTRAP_LOCATIONS_ENVIRONMENT = "IGLOO_BOOTSTRAP_LOCATIONS";

	/**
	 * System property to set to true if you want that alternative bootstrap locations replace default bootstrap
	 * locations. If not set, or false, alternative bootstrap locations are added to the default ones.
	 */
	public static final String BOOTSTRAP_OVERRIDE_DEFAULT_SYSTEM_PROPERTY = "igloo.bootstrapOverrideDefault";

	/**
	 * Environment variable to set to true if you want that alternative bootstrap locations replace default bootstrap
	 * locations. If not set, or false, alternative bootstrap locations are added to the default ones.
	 */
	public static final String BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT = "IGLOO_BOOTSTRAP_OVERRIDE_DEFAULT";

}
