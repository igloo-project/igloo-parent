package org.iglooproject.config.bootstrap.spring;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcesLevels;
import org.iglooproject.config.bootstrap.spring.config.BootstrapPropertySourcesConfiguration;
import org.iglooproject.config.bootstrap.spring.config.PropertySourcesLevelsConfiguration;
import org.iglooproject.config.bootstrap.spring.env.CompositeProtocolResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigRegistry;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * This initializer handles:
 *
 * <ul>
 *   <li>Loading of all {@link IglooPropertySourcesLevels} really early during context loading, to
 *       allow convenient {@link PropertySource} ordering, by including {@link
 *       PropertySourcesLevelsConfiguration}.
 *   <li>Loading of bootstrap configuration as a {@link PropertySource} by including {@link
 *       BootstrapPropertySourcesConfiguration}.
 *   <li>Build a custom log4j configuration, also based on bootstrap configuration.
 * </ul>
 */
public class ExtendedApplicationContextInitializer
    implements IApplicationContextInitializer,
        ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ExtendedApplicationContextInitializer.class);
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
        PropertySourcesLevelsConfiguration.class.getSimpleName(),
        PropertySourcesLevelsConfiguration.class);
    registerBean(
        applicationContext,
        BootstrapPropertySourcesConfiguration.class.getSimpleName(),
        BootstrapPropertySourcesConfiguration.class);
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

  private void loadLog4jConfiguration(ConfigurableApplicationContext applicationContext)
      throws IOException {
    // Customized log4j configuration. We combine multiple log4j files to build a new configuration.

    if (applicationContext.getEnvironment().getProperty(LOG4J2_CONFIGURATIONS_PROPERTY) != null) {
      @SuppressWarnings("unchecked")
      List<String> log4jLocations =
          Objects.requireNonNull(
              applicationContext
                  .getEnvironment()
                  .getProperty(LOG4J2_CONFIGURATIONS_PROPERTY, List.class));
      List<String> loadedConfigurations = Lists.newArrayList();
      List<String> ignoredConfigurations = Lists.newArrayList();

      loadLog4j2Configuration(
          applicationContext, log4jLocations, loadedConfigurations, ignoredConfigurations);

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
          LOG4J2_CONFIGURATIONS_PROPERTY);
    }
  }

  private void reconfigure(List<String> locations) {

    // modify configuration files list (comma-separated file path or url)
    // path without schemes are resolved as file, then as classpath resource
    // classpath resource must NOT start with a '/'
    System.setProperty("log4j2.configurationFile", String.join(",", locations));

    // reload configuration
    ((LoggerContext) LogManager.getContext(false)).reconfigure();
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
      reconfigure(locations);
    }
  }

  /**
   * Bootstrap configuration locations either from environment, system property or default
   * locations; first available win.
   *
   * @see
   *     AbstractExtendedApplicationContextInitializer#<getDefaultBootstrapConfigurationLocations>()
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
}
