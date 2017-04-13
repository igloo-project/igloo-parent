package fr.openwide.core.infinispan.utils;

import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationChildBuilder;
import org.infinispan.configuration.global.GlobalJmxStatisticsConfigurationBuilder;
import org.infinispan.configuration.global.TransportConfigurationBuilder;
import org.infinispan.jmx.PlatformMBeanServerLookup;

public class GlobalDefaultReplicatedTransientConfigurationBuilder extends GlobalConfigurationBuilder {

	public GlobalDefaultReplicatedTransientConfigurationBuilder() {
		super();
		// not planned to use JBoss, so we use directly PlatformMBeanServerLookup
		globalJmxStatistics().mBeanServerLookup(new PlatformMBeanServerLookup());
		transport().defaultTransport();
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
