package org.iglooproject.test.infinispan.util;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.iglooproject.infinispan.utils.DefaultReplicatedTransientConfigurationBuilder;
import org.iglooproject.infinispan.utils.GlobalDefaultReplicatedTransientConfigurationBuilder;
import org.iglooproject.test.infinispan.util.tasks.SimpleMessagingTask;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.common.base.Stopwatch;

public class TestCacheManagerBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessagingTask.class);

	public volatile static String PROCESS_ID = null;

	private final String name;

	private final Integer expectedViewSize;

	private final String taskName;

	private final String cacheName;

	public TestCacheManagerBuilder(String name, Integer expectedViewSize, String taskName, String cacheName) {
		this.name = name;
		this.expectedViewSize = expectedViewSize;
		this.taskName = taskName;
		this.cacheName = cacheName;
	}

	public TestCacheManagerBuilder(String name, String cacheName) {
		this(name, null, null, cacheName);
	}

	public EmbeddedCacheManager build() {
		PROCESS_ID = ManagementFactory.getRuntimeMXBean().getName().replaceAll("@.*", "");
		MDC.put("PID", PROCESS_ID);
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
				new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
		builder.configure(params.properties().setFileName("infinispan-test.properties"));
		try {
			// load configuration
			FileBasedConfiguration configuration = builder.getConfiguration();
			Properties properties = new Properties();
			// iterator to iterable trick
			for (String key : (Iterable<String>)() -> configuration.getKeys()) {
				properties.put(key, configuration.getString(key));
			}
			
			return buildFromConfiguration(properties);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Configuration loading failed", e);
		}
	}
	
	public EmbeddedCacheManager buildFromConfiguration(Properties properties) {
		GlobalConfiguration globalConfiguration =
				new GlobalDefaultReplicatedTransientConfigurationBuilder(properties)
					.cacheManagerName(cacheName).nodeName(name).build();
		Configuration configuration =
				new DefaultReplicatedTransientConfigurationBuilder().build();
		EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfiguration, configuration, false);
		cacheManager.getCache(TestConstants.CACHE_DEFAULT);
		
		if (expectedViewSize != null) {
			LOGGER.debug("Task: wait for view size = {}", expectedViewSize);
			Duration timeout = Duration.ofSeconds(30);
			Stopwatch watch = Stopwatch.createStarted();
			boolean viewSizeOk = false;
			while (watch.elapsed().compareTo(timeout) < 0) {
				if (cacheManager.getMembers().size() >= expectedViewSize) {
					viewSizeOk = true;
					break;
				}
			}
			if (!viewSizeOk) {
				LOGGER.error("Expected view size {} not reached before {} ms. timeout ; current view size {}",
						expectedViewSize, timeout.toMillis(), cacheManager.getMembers().size());
				throw new IllegalStateException("Expected view size not reached");
			}
			LOGGER.debug("Task: expected view size reached in {} ms.", watch.elapsed().toMillis());
		}
		
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
