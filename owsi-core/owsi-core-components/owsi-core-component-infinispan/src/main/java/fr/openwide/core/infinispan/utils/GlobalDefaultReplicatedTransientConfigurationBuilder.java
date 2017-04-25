package fr.openwide.core.infinispan.utils;

import java.util.Properties;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationChildBuilder;
import org.infinispan.configuration.global.GlobalJmxStatisticsConfigurationBuilder;
import org.infinispan.configuration.global.TransportConfigurationBuilder;
import org.infinispan.jmx.PlatformMBeanServerLookup;

public class GlobalDefaultReplicatedTransientConfigurationBuilder extends GlobalConfigurationBuilder {

	public GlobalDefaultReplicatedTransientConfigurationBuilder() {
		this(null);
	}

	public GlobalDefaultReplicatedTransientConfigurationBuilder(Properties transportProperties) {
		super();
		// not planned to use JBoss, so we use directly PlatformMBeanServerLookup
		globalJmxStatistics().mBeanServerLookup(new PlatformMBeanServerLookup());
		Properties properties = new Properties();
		if (transportProperties != null) {
			properties.putAll(transportProperties);
		}
		if ( ! properties.containsKey("configurationFile")) {
			// file available in classpath
			properties.put("configurationFile", "default-configs/default-jgroups-udp.xml");
		}
		transport().defaultTransport().withProperties(properties);
	}

	/**
	 * @see GlobalJmxStatisticsConfigurationBuilder#cacheManagerName(String)
	 */
	public GlobalConfigurationChildBuilder cacheManagerName(String cacheManagerName) {
		return globalJmxStatistics().enable().cacheManagerName(cacheManagerName).jmxDomain(cacheManagerName);
	}

	/**
	 * @see TransportConfigurationBuilder#nodeName(String)
	 */
	public GlobalConfigurationChildBuilder nodeName(String nodeName) {
		return transport().nodeName(nodeName);
	}

}
