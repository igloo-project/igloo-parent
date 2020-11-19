package org.iglooproject.config.bootstrap.spring.log4j1;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.iglooproject.config.bootstrap.spring.ILoggerConfiguration;

/**
 * Log4J 1.2 configuration reload from a custom {@link Properties}.
 * 
 * Class name is referenced in {@link ILoggerConfiguration}.
 */
@Deprecated
public class Log4J1Configuration implements ILoggerConfiguration {

	@Override
	public void reconfigure(Properties properties) {
		properties.setProperty("log4j.reset", "true");
		PropertyConfigurator.configure(properties);
	}

	@Override
	public void reconfigure(List<String> configurationFileList) {
		throw new IllegalStateException("Not implemented");
	}

}
