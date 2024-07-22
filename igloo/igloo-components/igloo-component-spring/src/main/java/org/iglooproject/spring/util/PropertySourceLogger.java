package org.iglooproject.spring.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SystemEnvironmentPropertySource;

/**
 * This Spring listener allows to log all PropertySource values. It is intended to check
 * configuration in case of configuration refactors (it allows to generate before and after
 * configuration, so that configuration can be diffed).
 *
 * <p>Properties are written to the file targeted by <em>igloo.propertySource.outputFileName</em>.
 * This property must be a path to a writable file. This file will be overwritten.
 *
 * @see PropertySource
 */
public class PropertySourceLogger implements ApplicationListener<ContextRefreshedEvent> {

  private static final String PROPERTY_IGLOO_PROPERTY_SOURCE_OUTPUT_FILE_NAME =
      "igloo.propertySource.outputFileName";
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertySourceLogger.class);

  /**
   * {@value} is the name given to the {@link PropertySource} for the set of {@linkplain
   * #mergeProperties() merged properties} supplied to this configurer.
   */
  public static final String LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME = "localProperties";

  /**
   * {@value} is the name given to the {@link PropertySource} that wraps the {@linkplain
   * #setEnvironment environment} supplied to this configurer.
   */
  public static final String ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME = "environmentProperties";

  @Autowired private ApplicationContext applicationContext;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent refresh) {
    // run only once by context
    if (refresh.getSource() instanceof AbstractApplicationContext
        && ((AbstractApplicationContext) refresh.getSource()).getParent() == null) {
      // retrieve application context
      ApplicationContext context = refresh.getApplicationContext();

      if (context
          .getEnvironment()
          .containsProperty(PROPERTY_IGLOO_PROPERTY_SOURCE_OUTPUT_FILE_NAME)) {
        LOGGER.info("PropertySource logging");

        // handle whole configuration writing
        logWholeSpringConfiguration(context);

        LOGGER.info("PropertySource logging end");
      } else {
        LOGGER.info(
            "PropertySource logging skipped ({} not set)",
            PROPERTY_IGLOO_PROPERTY_SOURCE_OUTPUT_FILE_NAME);
      }
    }
  }

  public Properties listProperties() {
    // store computed properties
    Properties properties = new OrderedProperties();
    // log text comment for not extracted properties
    final StringBuilder skippedProperties = new StringBuilder();
    // property names list, used for consistent ordering
    Set<String> propertyNames = Sets.newTreeSet();
    computeOrderedPropertyNames(applicationContext, skippedProperties, propertyNames);
    populateProperties(applicationContext, propertyNames, properties, skippedProperties);

    return properties;
  }

  /** Log all properties from registered {@link PropertySource}-s */
  private void logWholeSpringConfiguration(ApplicationContext context) {
    // store computed properties
    Properties properties = new OrderedProperties();
    // log text comment for not extracted properties
    final StringBuilder skippedProperties = new StringBuilder();
    // property names list, used for consistent ordering
    Set<String> propertyNames = Sets.newTreeSet();
    computeOrderedPropertyNames(context, skippedProperties, propertyNames);
    populateProperties(context, propertyNames, properties, skippedProperties);
    writeConfiguration(context, properties, skippedProperties);
  }

  private void populateProperties(
      ApplicationContext context,
      Set<String> propertyNames,
      Properties properties,
      final StringBuilder skippedProperties) {
    // previously, configuration was partly loaded in environment (bootstrap) and in
    // PropertySourcePlaceholderConfigurer. We try to load and to combine both.
    ConfigurableEnvironment environment = context.getBean(ConfigurableEnvironment.class);
    for (String propertyName : propertyNames) {
      try {
        String configurerProperty = environment.getProperty(propertyName); // null if not resolved
        String envProperty = context.getEnvironment().getProperty(propertyName);
        if (envProperty != null && !Objects.equals(configurerProperty, envProperty)) {
          properties.put(String.format("%s.configurer", propertyName), configurerProperty);
          properties.put(String.format("%s.environment", propertyName), envProperty);
        } else {
          properties.put(propertyName, configurerProperty);
        }
      } catch (IllegalArgumentException e) {
        // allow to skip environment variable as value like ${IFS+x} are not resolvable
        LOGGER.warn("{} : skipped", propertyName);
        skippedProperties.append(String.format("skipped: %s", propertyName)).append("\n");
      }
    }
  }

  private void computeOrderedPropertyNames(
      ApplicationContext context,
      final StringBuilder skippedProperties,
      Set<String> propertyNames) {
    ((ConfigurableEnvironment) context.getEnvironment())
        .getPropertySources()
        .forEach(
            p -> {
              if (p instanceof SystemEnvironmentPropertySource) {
                // skipped
              } else if (p instanceof EnumerablePropertySource<?>) {
                propertyNames.addAll(
                    Lists.newArrayList(((EnumerablePropertySource<?>) p).getPropertyNames()));
              } else {
                LOGGER.warn("{} is not enumerable", p);
                skippedProperties
                    .append(String.format("skipped PropertySource: %s as it is not enumerable", p))
                    .append("\n");
              }
            });
    ConfigurableEnvironment environment = context.getBean(ConfigurableEnvironment.class);
    environment
        .getPropertySources()
        .forEach(
            p -> {
              if (ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME.equals(p.getName())) {
                // skipped
              } else if (p instanceof EnumerablePropertySource<?>) {
                propertyNames.addAll(
                    Lists.newArrayList(((EnumerablePropertySource<?>) p).getPropertyNames()));
              } else {
                LOGGER.warn("{} is not enumerable", p);
                skippedProperties
                    .append(String.format("skipped PropertySource: %s as it is not enumerable", p))
                    .append("\n");
              }
            });
  }

  private void writeConfiguration(
      ApplicationContext context, Properties properties, final StringBuilder skippedProperties) {
    File file =
        context
            .getEnvironment()
            .getProperty(PROPERTY_IGLOO_PROPERTY_SOURCE_OUTPUT_FILE_NAME, File.class);
    try {
      properties.store(
          new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8),
          skippedProperties.toString());
    } catch (IOException e) {
      throw new IllegalStateException(
          String.format(
              "Configuration cannot be saved to %s",
              context
                  .getEnvironment()
                  .getProperty(PROPERTY_IGLOO_PROPERTY_SOURCE_OUTPUT_FILE_NAME)),
          e);
    }
  }

  private class OrderedProperties extends Properties {
    private static final long serialVersionUID = -2742222749532447185L;

    /** Java 11 ordering * */
    @Override
    public synchronized Enumeration<Object> keys() {
      return Collections.enumeration(new TreeSet<Object>(super.keySet()));
    }

    /** Java &lt; 11 ordering * */
    @Override
    public synchronized Set<Map.Entry<Object, Object>> entrySet() {
      return Collections.synchronizedSet(
          super.entrySet().stream()
              .sorted(Comparator.comparing(e -> e.getKey().toString()))
              .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
  }
  ;
}
