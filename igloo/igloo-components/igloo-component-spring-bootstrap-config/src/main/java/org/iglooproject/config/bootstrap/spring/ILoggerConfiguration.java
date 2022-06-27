package org.iglooproject.config.bootstrap.spring;

import java.util.List;
import java.util.Properties;

/**
 * Interface used to load correct implementation at runtime.
 */
public interface ILoggerConfiguration {

	void reconfigure(Properties properties);
	void reconfigure(List<String> configurationFileList);

	public enum LoggerImplementation {
		LOG4J("org.slf4j.impl.Log4jLoggerFactory", "org.iglooproject.config.bootstrap.spring.log4j1.Log4J1Configuration"),
		RELOAD4J("org.slf4j.impl.Reload4jLoggerFactory", "org.iglooproject.config.bootstrap.spring.log4j1.Log4J1Configuration"),
		LOG4J2("org.apache.logging.slf4j.Log4jLoggerFactory", "org.iglooproject.config.bootstrap.spring.log4j2.Log4J2Configuration");
		
		public final String slf4jLoggerFactoryClass;
		public final String configurationClass;
		
		private LoggerImplementation(String slf4jLoggerFactoryClass, String configurationClass) {
			this.slf4jLoggerFactoryClass = slf4jLoggerFactoryClass;
			this.configurationClass = configurationClass;
		}
	}

}
