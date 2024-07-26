package org.iglooproject.config.bootstrap.spring;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.iglooproject.config.bootstrap.spring.ILoggerConfiguration.LoggerImplementation;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcesLevels;
import org.iglooproject.config.bootstrap.spring.config.IglooBootstrapPropertySourcesConfig;
import org.iglooproject.config.bootstrap.spring.config.IglooPropertySourcesLevelsConfig;
import org.iglooproject.config.bootstrap.spring.env.CompositeProtocolResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigRegistry;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * This initializer handles:
 *
 * <ul>
 *   <li>Loading of all {@link IglooPropertySourcesLevels} really early during context loading, to
 *       allow convenient {@link PropertySource} ordering, by including {@link
 *       IglooPropertySourcesLevelsConfig}.
 *   <li>Loading of bootstrap configuration as a {@link PropertySource} by including {@link
 *       IglooBootstrapPropertySourcesConfig}.
 *   <li>Build a custom log4j configuration, also based on bootstrap configuration.
 * </ul>
 */
abstract class AbstractExtendedApplicationContextInitializer
    implements IApplicationContextInitializer,
        ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractExtendedApplicationContextInitializer.class);
  private static final Logger LOGGER_SYNTHETIC =
      LoggerFactory.getLogger(IGLOO_CONFIGURATION_LOGGER_NAME);

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    try {
      applicationContext.addProtocolResolver(new CompositeProtocolResolver());
      // add a configuration to preload all igloo @PropertySource levels
      // (used to control property overriding)
      registerIglooPropertySourcesLevels(applicationContext);
      // add configurations from bootstrap configuration to spring environment
      loadBootstrapConfiguration(applicationContext);
      // merge configured log4j configurations and reconfigure log4j
      loadLog4jConfiguration(applicationContext);
    } catch (IOException e) {
      throw new IllegalStateException("Error loading configuration files", e);
    }
  }

  private void registerIglooPropertySourcesLevels(
      ConfigurableApplicationContext applicationContext) {
    registerBean(
        applicationContext,
        IglooPropertySourcesLevelsConfig.class.getSimpleName(),
        IglooPropertySourcesLevelsConfig.class);
    registerBean(
        applicationContext,
        IglooBootstrapPropertySourcesConfig.class.getSimpleName(),
        IglooBootstrapPropertySourcesConfig.class);
  }

  protected void registerBean(
      ConfigurableApplicationContext applicationContext, String beanName, Class<?> beanClass) {
    if (applicationContext instanceof BeanDefinitionRegistry) {
      // GenericApplicationContext, AnnotationConfigApplicationContext : unit-test, cli cases
      RootBeanDefinition def2 = new RootBeanDefinition(beanClass);
      def2.setSource(null);
      registerBean((BeanDefinitionRegistry) applicationContext, def2, beanName);
    } else if (applicationContext instanceof AnnotationConfigRegistry) {
      // AnnotationConfigWebApplicationContext : servlet case
      ((AnnotationConfigRegistry) applicationContext).register(beanClass);
    }
  }

  private static BeanDefinitionHolder registerBean(
      BeanDefinitionRegistry registry, RootBeanDefinition definition, String beanName) {
    definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    registry.registerBeanDefinition(beanName, definition);
    return new BeanDefinitionHolder(definition, beanName);
  }

  private void loadBootstrapConfiguration(ConfigurableApplicationContext applicationContext)
      throws IOException {
    Collection<String> locations = getBootstrapConfigurationLocations(applicationContext);
    // preserve order !
    LinkedHashMap<String, ResourcePropertySource> resources = Maps.newLinkedHashMap();

    for (String location : locations) {
      if (resources.containsKey(location)) {
        LOGGER.debug("Bootstrap configuration: ignore duplicate {}", location);
      } else if (applicationContext.getResource(location).exists()) {
        LOGGER.debug("Bootstrap configuration: queuing {}", location);
        // Use an encoded resource; we enforce UTF-8 encoding
        Resource resource = applicationContext.getResource(location);
        EncodedResource encodedResource = new EncodedResource(resource, StandardCharsets.UTF_8);
        resources.put(location, new ResourcePropertySource(location, encodedResource));
      } else {
        LOGGER.debug("Bootstrap configuration: ignore not existing {}", location);
      }
    }

    if (resources.isEmpty()) {
      throw new IllegalStateException(
          String.format(
              "Bootstrap configuration: no configuration found from %s",
              Joiner.on(", ").join(locations)));
    }

    List<String> loadedLocations = Lists.newArrayList();
    List<String> reversedLocations = Lists.reverse(loadedLocations);
    loadedLocations.addAll(resources.keySet());

    if (LOGGER_SYNTHETIC.isInfoEnabled()) {
      LOGGER_SYNTHETIC.info(
          "Bootstrap configurations (ordered): {}", Joiner.on(", ").join(loadedLocations));
      List<String> ignoredLocations = Lists.newArrayList(locations);
      ignoredLocations.removeAll(loadedLocations);
      if (!ignoredLocations.isEmpty()) {
        LOGGER_SYNTHETIC.warn(
            "Bootstrap configurations **ignored**: {}", Joiner.on(",").join(ignoredLocations));
      }
    }

    for (String location : reversedLocations) {
      applicationContext.getEnvironment().getPropertySources().addLast(resources.get(location));
    }
  }

  private LoggerImplementation getLoggerImplementation() {
    String loggerFactoryClassName = LoggerFactory.getILoggerFactory().getClass().getName();
    if (loggerFactoryClassName.equals(LoggerImplementation.LOG4J2.slf4jLoggerFactoryClass)) {
      return LoggerImplementation.LOG4J2;
    } else {
      throw new IllegalStateException(
          String.format("Unknown LoggerFactory implementation %s", loggerFactoryClassName));
    }
  }

  private String getLoggerConfigurationsPropertyName(
      ConfigurableApplicationContext applicationContext, LoggerImplementation implementation) {
    switch (implementation) {
      case LOG4J2:
        return LOG4J2_CONFIGURATIONS_PROPERTY;
      default:
        throw new IllegalStateException(
            String.format("Unknown implementation %s", implementation.name()));
    }
  }

  private void loadLog4jConfiguration(ConfigurableApplicationContext applicationContext)
      throws IOException {
    // Customized log4j configuration. We combine multiple log4j files to build a new configuration.
    LoggerImplementation implementation = getLoggerImplementation();
    String loggerConfigurationsPropertyName =
        getLoggerConfigurationsPropertyName(applicationContext, implementation);
    if (applicationContext.getEnvironment().getProperty(loggerConfigurationsPropertyName) != null) {
      @SuppressWarnings("unchecked")
      List<String> log4jLocations =
          applicationContext
              .getEnvironment()
              .getProperty(loggerConfigurationsPropertyName, List.class);
      List<String> loadedConfigurations = Lists.newArrayList();
      List<String> ignoredConfigurations = Lists.newArrayList();
      switch (implementation) {
        case LOG4J2:
          loadLog4j2Configuration(
              applicationContext, log4jLocations, loadedConfigurations, ignoredConfigurations);
          break;
        default:
          throw new IllegalStateException(
              String.format("Unknown implementation %s", implementation.name()));
      }

      if (LOGGER_SYNTHETIC.isInfoEnabled()) {
        LOGGER_SYNTHETIC.info(
            "Log4j configurations (ordered): {}", Joiner.on(", ").join(loadedConfigurations));
        if (!ignoredConfigurations.isEmpty()) {
          LOGGER_SYNTHETIC.warn(
              "Log4j configurations **ignored**: {}", Joiner.on(",").join(ignoredConfigurations));
        }
      }
    } else {
      LOGGER_SYNTHETIC.warn(
          "Log4j: no {} configuration found; keeping default configuration",
          loggerConfigurationsPropertyName);
    }
  }

  /**
   * Perform reconfiguration by loading {@link ILoggerConfiguration} by class name (this allow to
   * put only needed implementation in classpath).
   *
   * @param configurationClassName
   * @param properties
   * @param locations
   */
  private void reconfigure(
      String configurationClassName, Properties properties, List<String> locations) {
    try {
      ILoggerConfiguration configuration =
          (ILoggerConfiguration)
              Class.forName(configurationClassName).getConstructor().newInstance();
      if (properties != null) {
        configuration.reconfigure(properties);
      } else {
        configuration.reconfigure(locations);
      }
    } catch (InvocationTargetException
        | NoSuchMethodException
        | RuntimeException
        | InstantiationException
        | IllegalAccessException
        | ClassNotFoundException e) {
      throw new IllegalStateException(
          String.format("Failed loading configuration reloading class %s", configurationClassName),
          e);
    }
  }

  /**
   * To load log4j2 configuration, we use built-in log4j.configurationFile that allows to specify
   * multiple configuration files.
   *
   * <p>We need to rewrite classpath: URLs as log4j2 does not use this type of url. It uses relative
   * path ; lookup is performed first as a file path, then as a classpath resource.
   */
  private void loadLog4j2Configuration(
      ConfigurableApplicationContext applicationContext,
      List<String> log4jLocations,
      List<String> loadedConfigurations,
      List<String> ignoredConfigurations) {
    // rework classpath:/ and classpath: urls -> use relative path ; log4j2 automatically searches
    // in classpath
    List<String> locations = new ArrayList<>();
    for (String location : log4jLocations) {
      if (applicationContext.getResource(location).exists()) {
        loadedConfigurations.add(location);
        if (location.startsWith("classpath:/")) {
          location = location.substring("classpath:/".length());
        }
        if (location.startsWith("classpath:")) {
          location = location.substring("classpath:".length());
        }
        LOGGER.info("Log4j : {} added", location);

        locations.add(location);
      } else {
        ignoredConfigurations.add(location);
        LOGGER.info("Log4j : {} not found", location);
      }
    }

    // reload only if files are available
    if (log4jLocations.isEmpty()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn(
            "Log4j : no additional files configured in {}", Joiner.on(",").join(log4jLocations));
      }
    } else {
      reconfigure(LoggerImplementation.LOG4J2.configurationClass, null, locations);
    }
  }

  /**
   * Bootstrap configuration locations either from environment, system property or default
   * locations; first available win.
   *
   * @see AbstractExtendedApplicationContextInitializer#getDefaultBootstrapConfigurationLocations()
   */
  private Collection<String> getBootstrapConfigurationLocations(
      ConfigurableApplicationContext applicationContext) {
    Collection<String> locations = Lists.newArrayList(getDefaultBootstrapConfigurationLocations());

    // check if we ignore default locations
    if (applicationContext
        .getEnvironment()
        .getProperty(BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT, Boolean.class, false)) {
      locations.clear();
      LOGGER.debug(
          "Ignoring default bootstrap configuration locations as {} is set",
          BOOTSTRAP_OVERRIDE_DEFAULT_ENVIRONMENT);
    }
    if (applicationContext
        .getEnvironment()
        .getProperty(BOOTSTRAP_OVERRIDE_DEFAULT_SYSTEM_PROPERTY, Boolean.class, false)) {
      locations.clear();
      LOGGER.debug(
          "Ignoring default bootstrap configuration locations as {} is set",
          BOOTSTRAP_OVERRIDE_DEFAULT_SYSTEM_PROPERTY);
    }

    // load first found configuration from environment variable ou system property
    @SuppressWarnings("unchecked")
    List<String> env =
        applicationContext
            .getEnvironment()
            .getProperty(BOOTSTRAP_LOCATIONS_ENVIRONMENT, List.class);
    if (env != null) {
      LOGGER.debug(
          "Using environment {}: {}",
          BOOTSTRAP_LOCATIONS_ENVIRONMENT,
          applicationContext.getEnvironment().getProperty(BOOTSTRAP_LOCATIONS_ENVIRONMENT));
      locations.addAll(env);
    }
    @SuppressWarnings("unchecked")
    List<String> system =
        applicationContext
            .getEnvironment()
            .getProperty(BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY, List.class);
    if (system != null) {
      LOGGER.debug(
          "Using environment {}: {}",
          BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY,
          applicationContext.getEnvironment().getProperty(BOOTSTRAP_LOCATIONS_SYSTEM_PROPERTY));
      locations.addAll(system);
    }

    return locations;
  }

  private Collection<String> getDefaultBootstrapConfigurationLocations() {
    return ImmutableList.<String>builder()
        .add("classpath:configuration-bootstrap-default.properties")
        .add("classpath:configuration-bootstrap.properties")
        .add("classpath:configuration-bootstrap-" + System.getProperty("user.name") + ".properties")
        .build();
  }

  /**
   * Return list of overriding configurations resources computed from bootstraped configuration.
   *
   * <p>Non existing or non readable locations are either ignored or throw an exception..
   *
   * <p>Returned list is not null and can be empty.
   *
   * @see #notFoundLocationThrowError
   */
  public static List<Resource> getLocationsFromBootstrapConfiguration(
      ApplicationContext applicationContext, boolean notFoundLocationThrowError) {
    List<Resource> locations = Lists.newArrayList();
    Environment environment = applicationContext.getEnvironment();
    if (environment.containsProperty(IGLOO_PROFILES_LOCATIONS_PROPERTY)) {
      @SuppressWarnings("unchecked")
      final List<String> profileLocations =
          (List<String>) environment.getProperty(IGLOO_PROFILES_LOCATIONS_PROPERTY, List.class);
      List<Resource> profileResources =
          profileLocations.stream()
              .map(resourceFromLocation(applicationContext, notFoundLocationThrowError))
              .filter(Objects::nonNull)
              .collect(Collectors.toList());
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
            "Loaded locations from {} value ({}): {}",
            // annotated type
            IGLOO_PROFILES_LOCATIONS_PROPERTY,
            environment.getProperty(IGLOO_PROFILES_LOCATIONS_PROPERTY),
            // list of loaded files
            Joiner.on(", ")
                .join(
                    profileResources.stream()
                        .map(AbstractExtendedApplicationContextInitializer::toURL)
                        .collect(Collectors.toList())));
      }

      locations.addAll(profileResources);
    } else {
      throw new IllegalStateException(
          String.format(
              "No bootstrap configuration found for %s. "
                  + "Configure %s as a spring context initializer.",
              IGLOO_PROFILES_LOCATIONS_PROPERTY,
              AbstractExtendedApplicationContextInitializer.class));
    }
    return locations;
  }

  /** Resource to a printable name. */
  private static final String toURL(Resource resource) {
    try {
      return resource.getURL().toString();
    } catch (IOException e) {
      // not reachable code as we filter location before constructing resources
      return String.format("%s", resource.getFilename());
    }
  }

  /**
   * Map a location to the corresponding resource. If resource is not readable, either throw an
   * exception or ignore the location. Ignored locations map to null.
   *
   * <p>If you map a collection, you need to filter null values after mapping.
   */
  private static Function<String, Resource> resourceFromLocation(
      ApplicationContext applicationContext, boolean throwErrorIfNotReadable) {
    return location -> {
      String resolvedLocation =
          applicationContext.getEnvironment().resolveRequiredPlaceholders(location);
      if (applicationContext.getResource(resolvedLocation).isReadable()) {
        LOGGER.debug("Configuration {} detected and added", location);
        return applicationContext.getResource(resolvedLocation);
      } else if (throwErrorIfNotReadable) {
        throw new IllegalStateException(
            String.format("Configuration %s does not exist or is not readable", location));
      } else {
        LOGGER.warn("Configuration {} not readable; ignored", location);
        return null;
      }
    };
  }
}
