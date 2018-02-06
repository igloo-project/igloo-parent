package org.iglooproject.test.infinispan.util;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.iglooproject.infinispan.utils.DefaultReplicatedTransientConfigurationBuilder;
import org.iglooproject.infinispan.utils.GlobalDefaultReplicatedTransientConfigurationBuilder;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.MDC;

public class TestCacheManagerBuilder {

	public volatile static String PROCESS_ID = null;

	private final String name;

	private final String taskName;

	private final String cacheName;


	public TestCacheManagerBuilder(String name, String taskName, String cacheName) {
		this.name = name;
		this.taskName = taskName;
		this.cacheName = cacheName;
	}

	public EmbeddedCacheManager build() {
		PROCESS_ID = ManagementFactory.getRuntimeMXBean().getName().replaceAll("@.*", "");
		MDC.put("PID", PROCESS_ID);
		
		// udp + multicast is difficult to obtain in test environment
		Properties properties = new Properties();
		properties.put("configurationFile", "test-jgroups-tcp.xml");
		
		GlobalConfiguration globalConfiguration =
				new GlobalDefaultReplicatedTransientConfigurationBuilder(properties)
					.cacheManagerName(cacheName).nodeName(name).build();
		Configuration configuration =
				new DefaultReplicatedTransientConfigurationBuilder().build();
		EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration, configuration, false);
		cacheManager.getCache(TestConstants.CACHE_DEFAULT);
		
		if (taskName != null) {
			try {
				Class<?> clazz = Class.forName(taskName);
				Constructor<?> constructor = clazz.getConstructor(EmbeddedCacheManager.class);
				Runnable r = (Runnable) constructor.newInstance(cacheManager);
				
				Thread t = new Thread((Runnable) r);
				t.start();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		
		return cacheManager;
	}

}
