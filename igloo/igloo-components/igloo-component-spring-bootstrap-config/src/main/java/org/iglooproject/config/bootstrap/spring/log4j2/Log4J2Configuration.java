package org.iglooproject.config.bootstrap.spring.log4j2;

import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.iglooproject.config.bootstrap.spring.ILoggerConfiguration;

/**
 * Log4J2 reconfiguration ; we use built-in support for multiple configuration files, by overriding
 * property log4j.configurationFile.
 *
 * <p>Class name is referenced in {@link ILoggerConfiguration}.
 */
public class Log4J2Configuration implements ILoggerConfiguration {

  @Override
  public void reconfigure(Properties properties) {
    throw new IllegalStateException("Not implemented");
  }

  @Override
  public void reconfigure(List<String> configurationLocations) {
    // modify configuration files list (comma-separated file path or url)
    // path without schemes are resolved as file, then as classpath resource
    // classpath resource must NOT start with a '/'
    System.setProperty("log4j2.configurationFile", String.join(",", configurationLocations));
    // reload log4j2 properties
    // TODO RFO SPRING 7
    PropertiesUtil.getProperties().reload();
    // reload configuration
    ((LoggerContext) LogManager.getContext(false)).reconfigure();
  }
}
