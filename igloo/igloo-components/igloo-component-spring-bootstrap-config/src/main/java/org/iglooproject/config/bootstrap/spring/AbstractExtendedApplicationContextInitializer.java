package org.iglooproject.config.bootstrap.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.ResourcePropertySource;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <p>
 * This initializer handles:
 * <ul>
 * <li>Load bootstrap configuration (list determined from environment or system property).</li>
 * <li>Build a custom log4j configuration.</li>
 * </ul>
 * </p>
 */
public abstract class AbstractExtendedApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	public static final String IGLOO_CONFIGURATION_LOGGER_NAME = "igloo@config";
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExtendedApplicationContextInitializer.class);
	private static final Logger LOGGER_SYNTHETIC = LoggerFactory.getLogger(IGLOO_CONFIGURATION_LOGGER_NAME);

	/**
	 * Log4j configurations to aggregate. This must be defined in bootstrap configuration.
	 */
	public static final String LOG4J_CONFIGURATIONS_PROPERTY = "igloo.log4j.configurationLocations";

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
	private static final String BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY = "igloo.bootstrapLocations";

	/**
	 * Environment variable to use to specify replacing or added bootstrap configurations.
	 * 
	 * @see #BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT
	 */
	private static final String BOOTSTRAP_LOCATIONS_ENVIRONMENT = "IGLOO_BOOTSTRAP_LOCATIONS";

	/**
	 * System property to set to true if you want that alternative bootstrap locations replace default bootstrap
	 * locations. If not set, or false, alternative bootstrap locations are added to the default ones.
	 */
	private static final String BOOTSTRAP_OVERRIDE_DEFAULT_SYSTEM_PROPERTY = "igloo.bootstrapOverrideDefault";

	/**
	 * Environment variable to set to true if you want that alternative bootstrap locations replace default bootstrap
	 * locations. If not set, or false, alternative bootstrap locations are added to the default ones.
	 */
	private static final String BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT = "IGLOO_BOOTSTRAP_OVERRIDE_DEFAULT";

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		try {
			// add configurations from bootstrap configuration to spring environment
			loadBootstrapConfiguration(applicationContext);
			// merge configured log4j configurations and reconfigure log4j
			loadLog4jConfiguration(applicationContext);
		} catch (IOException e) {
			throw new IllegalStateException("Error loading configuration files", e);
		}
	}

	private void loadBootstrapConfiguration(ConfigurableApplicationContext applicationContext) throws IOException {
		Collection<String> locations = getBootstrapConfigurationLocations(applicationContext);
		// preserve order !
		LinkedHashMap<String, ResourcePropertySource> resources = Maps.newLinkedHashMap();
		
		for (String location : locations) {
			if (resources.containsKey(location)) {
				LOGGER.debug("Bootstrap configuration: ignore duplicate {}", location);
			} else if (applicationContext.getResource(location).exists()) {
				LOGGER.debug("Bootstrap configuration: queuing {}", location);
				resources.put(location, new ResourcePropertySource(location));
			} else {
				LOGGER.debug("Bootstrap configuration: ignore not existing {}", location);
			}
		}
		
		if (resources.isEmpty()) {
			throw new IllegalStateException(String.format("Bootstrap configuration: no configuration found from %s",
					Joiner.on(", ").join(locations)));
		}
		
		List<String> loadedLocations = Lists.newArrayList();
		loadedLocations.addAll(resources.keySet());
		
		if (LOGGER_SYNTHETIC.isInfoEnabled()) {
			LOGGER_SYNTHETIC.info("Bootstrap configurations (ordered): {}", Joiner.on(", ").join(loadedLocations));
			List<String> ignoredLocations = Lists.newArrayList(locations);
			ignoredLocations.removeAll(loadedLocations);
			LOGGER_SYNTHETIC.warn("Bootstrap configurations **ignored**: {}", Joiner.on(",").join(ignoredLocations));
		}
		
		for (String location : loadedLocations) {
			applicationContext.getEnvironment().getPropertySources().addLast(resources.get(location));
		}
	}

	private void loadLog4jConfiguration(ConfigurableApplicationContext applicationContext) throws IOException {
		// Customized log4j configuration. We combine multiple log4j files to build a new configuration.
		if (applicationContext.getEnvironment().getProperty(LOG4J_CONFIGURATIONS_PROPERTY) != null) {
			@SuppressWarnings("unchecked")
			List<String> log4jLocations =
					applicationContext.getEnvironment().getProperty(LOG4J_CONFIGURATIONS_PROPERTY, List.class);
			List<String> loadedConfigurations = Lists.newArrayList();
			List<String> ignoredConfigurations = Lists.newArrayList();
			MutablePropertySources sources = new MutablePropertySources();
			boolean hasSource = false;
			List<String> propertyNames = new ArrayList<String>();
			for (String location : log4jLocations) {
				if (applicationContext.getResource(location).exists()) {
					loadedConfigurations.add(location);
					LOGGER.info(String.format("Log4j : %1$s added", location));
					hasSource = true;
					ResourcePropertySource source = new ResourcePropertySource(applicationContext.getResource(location));
					sources.addFirst(source);
					propertyNames.addAll(Arrays.asList(source.getPropertyNames()));
				} else {
					ignoredConfigurations.add(location);
					LOGGER.info(String.format("Log4j : %1$s not found", location));
				}
			}
			
			if (hasSource) {
				PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(sources);
				resolver.setPlaceholderPrefix("#{");
				resolver.setPlaceholderSuffix("}");
				Properties properties = new Properties();
				for (String propertyName : propertyNames) {
					if (resolver.containsProperty(propertyName)) {
						LOGGER.debug(String.format("Log4j : property resolved %1$s -> %2$s", propertyName, resolver.getProperty(propertyName)));
						properties.put(propertyName, resolver.getProperty(propertyName));
					} else {
						LOGGER.warn(String.format("Log4j : property %1$s cannot be resolved", propertyName));
					}
				}
				properties.setProperty("log4j.reset", "true");
				PropertyConfigurator.configure(properties);
			} else {
				LOGGER.warn("Log4j : no additional files configured in %1$s", Joiner.on(",").join(log4jLocations));
			}
			
			if (LOGGER_SYNTHETIC.isInfoEnabled()) {
				LOGGER_SYNTHETIC.info("Log4j configurations (ordered): {}", Joiner.on(", ").join(loadedConfigurations));
				LOGGER_SYNTHETIC.warn("Log4j configurations **ignored**: {}", Joiner.on(",").join(ignoredConfigurations));
			}
		} else {
			LOGGER_SYNTHETIC.warn("Log4j: no {} configuration found; keeping default configuration", LOG4J_CONFIGURATIONS_PROPERTY);
		}
	}

	/**
	 * Bootstrap configuration locations either from environment, system property or default locations;
	 * first available win.
	 * 
	 * @see AbstractExtendedApplicationContextInitializer#getDefaultBootstrapConfigurationLocations()
	 */
	private Collection<String> getBootstrapConfigurationLocations(ConfigurableApplicationContext applicationContext) {
		Collection<String> locations = Lists.newArrayList(getDefaultBootstrapConfigurationLocations());
		
		// check if we ignore default locations
		if (applicationContext.getEnvironment().getProperty(BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT, Boolean.class, false)) {
			locations.clear();
			LOGGER.debug("Ignoring default bootstrap configuration locations as {} is set", BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT);
		}
		if (applicationContext.getEnvironment().getProperty(BOOTSTRAP_OVERRIDE_DEFAULT_SYSTEM_PROPERTY, Boolean.class, false)) {
			locations.clear();
			LOGGER.debug("Ignoring default bootstrap configuration locations as {} is set", BOOTSTRAP_OVERRIDE_DEFAULT_SYSTEM_PROPERTY);
		}
		
		// load first found configuration from environment variable ou system property
		@SuppressWarnings("unchecked")
		List<String> env = applicationContext.getEnvironment().getProperty(BOOTSTRAP_LOCATIONS_ENVIRONMENT, List.class);
		if (env != null) {
			LOGGER.debug("Using environment {}: {}", BOOTSTRAP_LOCATIONS_ENVIRONMENT,
					applicationContext.getEnvironment().getProperty(BOOTSTRAP_LOCATIONS_ENVIRONMENT));
			locations.addAll(env);
		}
		@SuppressWarnings("unchecked")
		List<String> system = applicationContext.getEnvironment().getProperty(BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY, List.class);
		if (system != null) {
			LOGGER.debug("Using environment {}: {}", BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY,
					applicationContext.getEnvironment().getProperty(BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY));
			locations.addAll(system);
		}
		
		return locations;
	}

	private Collection<String> getDefaultBootstrapConfigurationLocations() {
		return ImmutableList.<String>builder()
				.add("classpath:configuration-bootstrap-default.properties")
				.add("classpath:configuration-bootstrap.properties")
				.add("classpath:configuration-bootstrap-" + System.getProperty("user.name") + ".properties").build();
	}

}
