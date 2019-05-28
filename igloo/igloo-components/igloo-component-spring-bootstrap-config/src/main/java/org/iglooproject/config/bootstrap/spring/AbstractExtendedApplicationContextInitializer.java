package org.iglooproject.config.bootstrap.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.Resource;
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
abstract class AbstractExtendedApplicationContextInitializer implements IApplicationContextInitializer,
		ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExtendedApplicationContextInitializer.class);
	private static final Logger LOGGER_SYNTHETIC = LoggerFactory.getLogger(IGLOO_CONFIGURATION_LOGGER_NAME);

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
		List<String> reversedLocations = Lists.reverse(loadedLocations);
		loadedLocations.addAll(resources.keySet());
		
		if (LOGGER_SYNTHETIC.isInfoEnabled()) {
			LOGGER_SYNTHETIC.info("Bootstrap configurations (ordered): {}", Joiner.on(", ").join(loadedLocations));
			List<String> ignoredLocations = Lists.newArrayList(locations);
			ignoredLocations.removeAll(loadedLocations);
			if (! ignoredLocations.isEmpty()) {
				LOGGER_SYNTHETIC.warn("Bootstrap configurations **ignored**: {}", Joiner.on(",").join(ignoredLocations));
			}
		}
		
		for (String location : reversedLocations) {
			applicationContext.getEnvironment().getPropertySources().addLast(resources.get(location));
		}
		
		loadIglooConfigurationLocations(applicationContext);
	}

	private void loadIglooConfigurationLocations(ConfigurableApplicationContext applicationContext) {
		List<Resource> resources =
				ApplicationConfigurerBeanFactoryPostProcessor.getLocationsFromBootstrapConfiguration(applicationContext, false);
		CompositePropertySource propertySource = new CompositePropertySource("igloo.configurationLocations");
		for (Resource resource : resources) {
			try {
				propertySource.addFirstPropertySource(new ResourcePropertySource(resource));
			} catch (IOException e) {
				throw new IllegalStateException(String.format("Error loading resource %s", resource.getDescription()), e);
			}
		}
		applicationContext.getEnvironment().getPropertySources().addFirst(propertySource);
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
				if (! ignoredConfigurations.isEmpty()) {
					LOGGER_SYNTHETIC.warn("Log4j configurations **ignored**: {}", Joiner.on(",").join(ignoredConfigurations));
				}
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
