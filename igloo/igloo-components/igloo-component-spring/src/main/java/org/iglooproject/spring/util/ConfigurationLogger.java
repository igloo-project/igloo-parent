package org.iglooproject.spring.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * This Spring listener allows to log PropertyId-s values on refresh event. It is triggered on by a
 * root context event.
 *
 * <p>Logged properties are controlled by <i>propertyNamesForInfoLogLevel</i> attribute, that list
 * PropertyId-s keys to log. These string values must match a registered PropertyId key.
 *
 * <p>Logged values are sent to {@link org.iglooproject.spring.util.ConfigurationLogger} with an
 * INFO level, and according to <i>propertyNamesForInfoLogLevel</i> list order.
 *
 * <p>If {@link org.iglooproject.spring.util.ConfigurationLogger} is set to TRACE level, all
 * registered PropertyId-s are logged in a second phase.
 *
 * <p>logPattern attribute allows to control the way key/value are logged.
 *
 * @see PropertyId
 * @see IConfigurablePropertyService
 */
public class ConfigurationLogger implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLogger.class);

  private String logPattern = "%1$30s : %2$s";

  private List<String> propertyIdsKeysForInfoLogLevel = Lists.newArrayList();

  public void setPropertyNamesForInfoLogLevel(String propertyIds) {
    propertyIdsKeysForInfoLogLevel.addAll(StringUtils.splitAsList(propertyIds, ","));
  }

  public void setLogPattern(String logPattern) {
    this.logPattern = logPattern;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent refresh) {
    // run only once by context
    if (refresh.getSource() instanceof AbstractApplicationContext
        && ((AbstractApplicationContext) refresh.getSource()).getParent() == null) {
      LOGGER.info("Configuration logging");

      // retrieve application context
      ApplicationContext context = refresh.getApplicationContext();

      // retrieve IConfigurablePropertyService
      IConfigurablePropertyService propertyService = getPropertyServiceName(context);
      if (propertyService != null) {
        LOGGER.info("Configuration found, start logging");

        /* Log PropertyId-s  */
        logConfigurationPropertiesIds(propertyService);
      } else {
        // warn if no IPropertyService found
        if (LOGGER.isWarnEnabled()) {
          LOGGER.warn(
              String.format(
                  "No %1$s found. Unable to log the configuration.",
                  IConfigurablePropertyService.class.getSimpleName()));
        }
      }

      LOGGER.info("Configuration logging end");
    }
  }

  /**
   * IConfigurablePropertyService should be unique. A warn log is thrown if multiple beans are found
   */
  private IConfigurablePropertyService getPropertyServiceName(ApplicationContext context) {
    String[] propertyServiceNames = context.getBeanNamesForType(IConfigurablePropertyService.class);
    if (propertyServiceNames.length == 0) {
      return null;
    }
    String propertyServiceName = propertyServiceNames[0];
    if (propertyServiceNames.length > 1) {
      if (LOGGER.isWarnEnabled()) { // NOSONAR (squid:S1066)
        LOGGER.warn(
            String.format(
                "Multiple %1$s found. We only log the configuration of the first instance.",
                IPropertyService.class.getSimpleName()));
      }
    }
    return (IConfigurablePropertyService) context.getBean(propertyServiceName);
  }

  /**
   * Log configured PropertyId-s values, according to configuration order, then, if TRACE mode
   * enabled, log all PropertyId-s values.
   */
  private void logConfigurationPropertiesIds(IConfigurablePropertyService propertyService) {
    // store logged properties to log each property only once
    List<String> loggedProperties = Lists.newArrayList();
    @SuppressWarnings("unchecked")
    Iterable<PropertyId<?>> registeredPropertyIds =
        (Iterable<PropertyId<?>>)
            (Object) Iterables.filter(propertyService.listRegistered(), PropertyId.class);

    // iterate on propertyIdsKeysForInfoLogLevel so that properties are logged according to
    // configuration order.
    for (String propertyIdKey : propertyIdsKeysForInfoLogLevel) {
      if (loggedProperties.contains(propertyIdKey)) {
        continue;
      }

      for (PropertyId<?> propertyId : registeredPropertyIds) {
        if (propertyId.getKey().equals(propertyIdKey)) {
          logPropertyAsInfo(propertyIdKey, propertyService.get(propertyId));
          loggedProperties.add(propertyIdKey);
          break;
        }
      }
    }

    /* If LOGGER configured at TRACE level, logging all PropertyId-s after the configured ones */
    if (LOGGER.isTraceEnabled()) {
      for (PropertyId<?> propertyId : registeredPropertyIds) {
        if (!propertyIdsKeysForInfoLogLevel.contains(propertyId.getKey())) {
          logPropertyAsTrace(propertyId.getKey(), propertyService.get(propertyId));
        }
      }
    }
  }

  private void logPropertyAsInfo(String propertyName, Object value) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(String.format(logPattern, propertyName, value));
    }
  }

  private void logPropertyAsTrace(String propertyName, Object value) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.trace(String.format(logPattern, propertyName, value));
    }
  }
}
