package org.iglooproject.config.bootstrap.spring;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

/**
 * Process {@link Configuration} beans annotated {@link ManifestPropertySource} to extract MANIFEST informations
 * in properties. A prefix is used before each property.
 */
public class ManifestPropertySourceBeanFactoryPostProcessor implements ApplicationContextAware, BeanFactoryPostProcessor, PriorityOrdered {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManifestPropertySourceBeanFactoryPostProcessor.class);

	private static final String PROPERTY_SOURCE_NAME_PREFIX = "__MANIFEST_PROPERY_SOURCE__";

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// go through all bean definitions to find application name
		// use entry as a pair
		List<Entry<Class<?>, ManifestPropertySource>> propertySources = Arrays.stream(beanFactory.getBeanDefinitionNames())
			.map(this.getBeanType(beanFactory))		// get unwrapped bean type (for cglib wrapped beans)
			.filter(Objects::nonNull)		// ignore null values
			// find @ManifestPropertySource
			.filter(ManifestPropertySourceBeanFactoryPostProcessor::isManifestPropertySourceBeanType)
			// map to @ManifestPropertySource
			.map(ManifestPropertySourceBeanFactoryPostProcessor::toManifestPropertySource)
			.collect(Collectors.toList());

		for (Entry<Class<?>, ManifestPropertySource> manifestPropertySource : propertySources) {
			String prefix = manifestPropertySource.getValue().prefix();
			Class<?> clazz = manifestPropertySource.getValue().clazz();
			boolean failIfNotFound = manifestPropertySource.getValue().failIfNotFound();
			String className = null;
			if (clazz == null || clazz == void.class) {
				className = manifestPropertySource.getValue().className();
				if (className != null && className.length() > 0) {
					try {
						clazz = getClass().getClassLoader().loadClass(className);
					} catch (ClassNotFoundException e) {
						String message = String.format("%s not found in classpath.", className);
						if (failIfNotFound) {
							throw new RuntimeException(message, e);
						} else {
							LOGGER.warn(message, e);
						}
					}
				}
			}
			if (className != null && clazz == null) {
				LOGGER.warn("{} className not found on @{} on {} : skipped",
						className, ManifestPropertySource.class.getSimpleName(), manifestPropertySource.getKey().getSimpleName());
				continue;
			}
			if (clazz == null || clazz == void.class) {
				clazz = manifestPropertySource.getKey();
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Extracting MANIFEST information from {}", clazz.getSimpleName());
			}
			Manifest manifest = findManifest(clazz);
			Map<String, Object> properties = new HashMap<>();
			for (Entry<Object, Object> attributeEntry : manifest.getMainAttributes().entrySet()) {
				properties.put(String.format("%s.%s", prefix, attributeEntry.getKey()), attributeEntry.getValue());
			}
			for (Entry<String, Attributes> manifestEntry : manifest.getEntries().entrySet()) {
				for (Entry<Object, Object> attributeEntry : manifestEntry.getValue().entrySet()) {
					properties.put(String.format("%s.%s.%s", prefix, manifestEntry.getKey(), attributeEntry.getKey()), attributeEntry.getValue());
				}
			}
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Loading property for {}:\n{}", clazz, Joiner.on(",\n").withKeyValueSeparator(": ").join(properties));
			}
			
			String propertySourceName = PROPERTY_SOURCE_NAME_PREFIX + clazz.getSimpleName();
			
			PropertySource<?> existingPropertySource = applicationContext.getEnvironment().getPropertySources().remove(propertySourceName);
			if (existingPropertySource != null) {
				LOGGER.warn("{} already exists and is removed", propertySourceName);
			}
			applicationContext.getEnvironment().getPropertySources().addLast(
					new MapPropertySource(propertySourceName, properties)
			);
		}
	}

	private Manifest findManifest(Class<?> clazz) {
		String className = clazz.getSimpleName();
		String classRelativeFile = className + ".class";
		URL clazzUrl = clazz.getResource(classRelativeFile);
		final String manifestPath;
		LOGGER.info("Find MANIFEST.MF from class {}", clazzUrl);
		if (clazzUrl.getProtocol().equals("jar")) {
			manifestPath = clazzUrl.toString().substring(0, clazzUrl.toString().lastIndexOf("!") + 1) + 
						"/META-INF/MANIFEST.MF";
		} else if (clazzUrl.getProtocol().equals("file")) {
			// 0 dot -> packageLevel = 0, 1 dot -> packageLevel = 1, 2 dot -> packageLevel = 2, ...
			int packageLevel = Pattern.compile(Pattern.quote(".")).splitAsStream(clazz.getName()).collect(Collectors.counting()).intValue() - 1;
			
			// if class at rootLevel, packageLevel = 0, no ../ is needed
			String folderPath = clazzUrl.toString();
			// file:path/file.class -> file:path
			folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
			for (int level = 0; level < packageLevel; level++) {
				// file:path1/segment -> file:path1
				folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
			}
			manifestPath = folderPath + "/META-INF/MANIFEST.MF";
		} else {
			throw new RuntimeException(String.format("Protocol %s not supported in URL %s", clazzUrl.getProtocol(), clazzUrl.toString()));
		}
		LOGGER.info("Trying to load MANIFEST.MF from {}", manifestPath);
		try (InputStream is = new URL(manifestPath).openStream()) {
			Manifest manifest = new Manifest(is);
			return manifest;
		} catch (IOException e) {
			throw new RuntimeException(String.format("Failure to read MANIFEST at url: %s", manifestPath, e));
		}
	}

	private Function<String, Class<?>> getBeanType(ConfigurableListableBeanFactory beanFactory) {
		return beanName -> ClassUtils.getUserClass(beanFactory.getType(beanName));
	}

	/**
	 * Check if beanType is an {@link ManifestPropertySource} annotated one. Used to filter streams.
	 */
	private static boolean isManifestPropertySourceBeanType(Class<?> beanType) {
		return isConfigurationBeanType(beanType) && beanType.isAnnotationPresent(ManifestPropertySource.class);
	}

	/**
	 * Check if beanType is an {@link Configuration} annotated one. Used to filter streams.
	 */
	private static boolean isConfigurationBeanType(Class<?> beanType) {
		return beanType.isAnnotationPresent(Configuration.class);
	}

	/**
	 * {@link ApplicationContext} used to load {@link Resource} with Spring API.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (applicationContext instanceof ConfigurableApplicationContext) {
			this.applicationContext = (ConfigurableApplicationContext) applicationContext;
		} else {
			throw new BeanNotOfRequiredTypeException("applicationContext", ConfigurableApplicationContext.class,
					applicationContext.getClass());
		}
	}

	/**
	 * Map a stream of {@link ManifestPropertySource}
	 */
	private static Entry<Class<?>, ManifestPropertySource> toManifestPropertySource(Class<?> beanType) {
		return Iterables.getOnlyElement(ImmutableMap.<Class<?>, ManifestPropertySource>builder().put(beanType, beanType.getAnnotation(ManifestPropertySource.class)).build().entrySet(), null);
	}

	/**
	 * Perform after {@link ApplicationConfigurerBeanFactoryPostProcessor}
	 */
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 100;
	}

}
