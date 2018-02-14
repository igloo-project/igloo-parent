package org.iglooproject.spring.config.spring.annotation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.Resource;

import com.google.common.base.Charsets;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MoreCollectors;
import com.google.common.collect.Multimap;

/**
 * <p>This {@link BeanFactoryPostProcessor} extracts {@link ApplicationDescription} and {@link ConfigurationLocations}
 * informations to prepare configuration loading.</p>
 * 
 * <p>If used, Spring context must declare an empty {@link PropertySourcesPlaceholderConfigurer} bean without
 * locations.</p>
 */
public class ApplicationConfigurerBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware,
		PriorityOrdered {

	private ApplicationContext applicationContext;

	/**
	 * Extract {@literal @}{@link ApplicationDescription} and {@literal @}{@link ConfigurationLocations} from
	 * {@literal @}{@link Configuration} beans.
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		PropertySourcesPlaceholderConfigurer configurer = beanFactory.getBean(PropertySourcesPlaceholderConfigurer.class);
		
		Map<Integer, List<Resource>> locationGroups = Maps.newHashMap();
		
		// go through all bean definitions to find application name
		Stream<String> applicationNames = Arrays.stream(beanFactory.getBeanDefinitionNames())
			.map(beanFactory::getType)		// get unwrapped bean type (for cglib wrapped beans)
			.filter(Objects::nonNull)		// ignore null values
			// find ApplicationDescription
			.filter(ApplicationConfigurerBeanFactoryPostProcessor::isApplicationDescriptionBeanType)
			// map to ApplicationDescription.name()
			.map(ApplicationConfigurerBeanFactoryPostProcessor::applicationDescriptionBeanTypeToName);
		
		final String applicationName;
		try {
			applicationName = applicationNames.collect(MoreCollectors.onlyElement());
		} catch (NoSuchElementException e) {
			throw new ApplicationContextException(String.format("No @%s bean annotated with @%s.name()",
					Configuration.class, ApplicationDescription.class));
		} catch (IllegalArgumentException e) {
			throw new ApplicationContextException(String.format("@%s must be unique; %d definitions found",
					ApplicationDescription.class.getSimpleName(), applicationNames.collect(Collectors.counting())));
		}
		
		Multimap<Integer, Resource> locationsByOrder = LinkedListMultimap.create();
		// go through all bean definitions to find application name
		Arrays.stream(beanFactory.getBeanDefinitionNames())
			.map(beanFactory::getType)		// get unwrapped bean type (for cglib wrapped beans)
			.filter(Objects::nonNull)		// ignore null values
			// find ConfigurationLocation
			.filter(ApplicationConfigurerBeanFactoryPostProcessor::isConfigurationLocationsBeanType)
			// store configured resources in locationsByOrder
			.forEachOrdered(this.configurationLocationsBeanTypeToLocations(locationsByOrder, applicationName));
		
		// order provided configurations
		List<Resource> locations = Lists.newArrayList();
		List<Integer> orders = Lists.newArrayList(locationGroups.keySet());
		Collections.sort(orders);
		for (Integer order : orders) {
			locations.addAll(locationGroups.get(order));
		}
		
		// convention: we use UTF-8
		configurer.setFileEncoding(Charsets.UTF_8.name());
		// this files override XML defined properties
		configurer.setLocalOverride(false);
		configurer.setIgnoreResourceNotFound(true);
		configurer.setLocations(locations.toArray(new Resource[locations.size()]));
	}

	/**
	 * Check if beanType is an {@link ApplicationDescription} annotated one. Used to filter streams.
	 */
	private static boolean isApplicationDescriptionBeanType(Class<?> beanType) {
		return isConfigurationBeanType(beanType) && beanType.isAnnotationPresent(ApplicationDescription.class);
	}

	/**
	 * Check if beanType is an {@link ConfigurationLocations} annotated one. Used to filter streams.
	 */
	private static boolean isConfigurationLocationsBeanType(Class<?> beanType) {
		return isConfigurationBeanType(beanType) && beanType.isAnnotationPresent(ConfigurationLocations.class);
	}

	/**
	 * Map a stream of {@link ApplicationDescription} aware beans to {@link ApplicationDescription#name()}
	 */
	private static String applicationDescriptionBeanTypeToName(Class<?> beanType) {
		return beanType.getAnnotation(ApplicationDescription.class).name();
	}

	/**
	 * Check if beanType is an {@link Configuration} annotated one. Used to filter streams.
	 */
	private static boolean isConfigurationBeanType(Class<?> beanType) {
		return beanType.isAnnotationPresent(Configuration.class);
	}

	/**
	 * This method return a consumer that inspect beanType annotated with {@link ConfigurationLocations}
	 * and store targetted configuration resources in the provided multimap.
	 */
	private Consumer<Class<?>> configurationLocationsBeanTypeToLocations(
			Multimap<Integer, Resource> locations, String applicationName) {
		return (Class<?> beanType) -> {
			ConfigurationLocations annotation = beanType.getAnnotation(ConfigurationLocations.class);
			
			// load configuration with provided IConfigurationLocationProvider
			Class<? extends IConfigurationLocationProvider> configurationLocationProviderClass =
					annotation.configurationLocationProvider();
			IConfigurationLocationProvider provider = BeanUtils.instantiateClass(configurationLocationProviderClass,
					IConfigurationLocationProvider.class);
			
			List<Resource> typeLocations = Lists.newArrayList();
			// récupération des locations et répartition par numéro d'ordre
			for (String location : provider.getLocations(
					applicationName,
					applicationContext.getEnvironment().getProperty("environment", "default"),
					annotation.locations())) {
				typeLocations.add(applicationContext.getResource(location));
			}
			locations.putAll(annotation.order(), typeLocations);
		};
	}

	/**
	 * {@link ApplicationContext} used to load {@link Resource} with Spring API.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * We need this {@link BeanFactoryPostProcessor} to be processed before {@link PropertySourcesPlaceholderConfigurer}
	 */
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1;
	}

}
