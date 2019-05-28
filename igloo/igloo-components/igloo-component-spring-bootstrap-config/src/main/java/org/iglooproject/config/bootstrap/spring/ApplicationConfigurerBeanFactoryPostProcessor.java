package org.iglooproject.config.bootstrap.spring;

import static org.iglooproject.config.bootstrap.spring.IApplicationContextInitializer.IGLOO_APPLICATION_NAME_PROPERTY;
import static org.iglooproject.config.bootstrap.spring.IApplicationContextInitializer.IGLOO_CONFIGURATION_LOGGER_NAME;
import static org.iglooproject.config.bootstrap.spring.IApplicationContextInitializer.IGLOO_PROFILES_LOCATIONS_PROPERTY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.iglooproject.config.bootstrap.spring.annotations.ConfigurationLocations;
import org.iglooproject.functional.Function2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigurerBeanFactoryPostProcessor.class);
	private static final Logger LOGGER_SYNTHETIC = LoggerFactory.getLogger(IGLOO_CONFIGURATION_LOGGER_NAME);

	private final boolean notFoundLocationThrowError;

	private ConfigurableApplicationContext applicationContext;

	public ApplicationConfigurerBeanFactoryPostProcessor() {
		this(true);
	}

	public ApplicationConfigurerBeanFactoryPostProcessor(boolean notFoundLocationThrowError) {
		super();
		this.notFoundLocationThrowError = notFoundLocationThrowError;
	}

	/**
	 * Extract {@literal @}{@link ApplicationDescription} and {@literal @}{@link ConfigurationLocations} from
	 * {@literal @}{@link Configuration} beans.
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		PropertySourcesPlaceholderConfigurer configurer = beanFactory.getBean(PropertySourcesPlaceholderConfigurer.class);
		
		configureApplicationDescription(beanFactory);
		
		List<Resource> bootstrapedConfigurations = getLocationsFromBootstrapConfiguration(applicationContext,
				notFoundLocationThrowError);
		if (bootstrapedConfigurations.isEmpty()) {
			LOGGER.info("Bootstraped-configured configurations is empty (overriding configurations missing)");
		}
		
		List<Resource> defaultLocations = loadLocationsFromApplicationConfigurations(beanFactory);
		if (defaultLocations.isEmpty()) {
			LOGGER.info("Default configurations is empty (overridable configurations missing)");
		}
		
		List<Resource> locations = Lists.newArrayList();
		// added so that bootstraped configurations override default ones
		locations.addAll(defaultLocations);
		locations.addAll(bootstrapedConfigurations);
		
		if (LOGGER_SYNTHETIC.isInfoEnabled()) {
			LOGGER_SYNTHETIC.info("Spring configurations (ordered): {}",
					Joiner.on(", ").join(locations.stream()
							.map(ApplicationConfigurerBeanFactoryPostProcessor::toURL)
							.collect(Collectors.toList())));
		}
		
		// convention: we use UTF-8
		configurer.setFileEncoding(Charsets.UTF_8.name());
		// this files override XML defined properties
		configurer.setLocalOverride(true);
		configurer.setIgnoreResourceNotFound(false);
		configurer.setLocations(locations.toArray(new Resource[locations.size()]));
	}

	/**
	 * <p>Stateful behavior related to {@link #applicationContext}.</p>
	 * 
	 * <p>Inject in Spring environment configurations from {@link ApplicationDescription} (currently, only
	 * {@link AbstractExtendedApplicationContextInitializer#IGLOO_APPLICATION_NAME_PROPERTY}.</p>
	 * 
	 * <p>Fatal error if {@link ApplicationDescription} is not found or inconsistent (multiple configurations).</p>
	 */
	private void configureApplicationDescription(ConfigurableListableBeanFactory beanFactory) {
		// go through all bean definitions to find application name
		List<String> applicationNames = Arrays.stream(beanFactory.getBeanDefinitionNames())
			.map(this.getBeanType(beanFactory))		// get unwrapped bean type (for cglib wrapped beans)
			.filter(Objects::nonNull)		// ignore null values
			// find ApplicationDescription
			.filter(ApplicationConfigurerBeanFactoryPostProcessor::isApplicationDescriptionBeanType)
			// map to ApplicationDescription.name()
			.map(ApplicationConfigurerBeanFactoryPostProcessor::applicationDescriptionBeanTypeToName)
			.filter(StringUtils::hasLength)
			.collect(Collectors.toList());
		// we need to collect result, as if anything fails, stream (as based on beanFactory) will be broken
		
		try {
			final String applicationName = applicationNames.stream().collect(MoreCollectors.onlyElement());
			// this allows to use ${igloo.applicationName} placeholder in file locations
			LOGGER.info("Bootstrap configuration: setting {} to {}", IGLOO_APPLICATION_NAME_PROPERTY, applicationName);
			applicationContext.getEnvironment().getPropertySources()
				.addLast(new MapPropertySource(
						"applicationName",
						ImmutableMap.<String, Object>builder().put(IGLOO_APPLICATION_NAME_PROPERTY, applicationName).build()
				)
		);
		} catch (NoSuchElementException e) {
			throw new ApplicationContextException(String.format("No @%s bean annotated with @%s.name()",
					Configuration.class, ApplicationDescription.class));
		} catch (IllegalArgumentException e) {
			throw new ApplicationContextException(String.format("@%s must be unique; %d definitions found",
					ApplicationDescription.class.getSimpleName(), applicationNames.stream().collect(Collectors.counting())));
		}
	}

	private Function2<String, Class<?>> getBeanType(ConfigurableListableBeanFactory beanFactory) {
		return beanName -> ClassUtils.getUserClass(beanFactory.getType(beanName));
	}

	/**
	 * <p>Stateless. Extract configuration locations from {@link ConfigurationLocations} annotations. Placeholders
	 * available in Spring environment (loaded from system properties, environment variables and bootstraped
	 * configuration) are allowed.</p>
	 * 
	 * <p>Non existing or non readable locations are either ignored or throw an exception.</p>.
	 * 
	 * <p>Returned list is not null and can be empty.</p>
	 * 
	 * @see #notFoundLocationThrowError
	 */
	private List<Resource> loadLocationsFromApplicationConfigurations(ConfigurableListableBeanFactory beanFactory) {
		Multimap<Integer, Resource> locationsByOrder = LinkedListMultimap.create();
		// go through all bean definitions to find application name
		Arrays.stream(beanFactory.getBeanDefinitionNames())
			.map(this.getBeanType(beanFactory))		// get unwrapped bean type (for cglib wrapped beans)
			.filter(Objects::nonNull)		// ignore null values
			// find ConfigurationLocation
			.filter(ApplicationConfigurerBeanFactoryPostProcessor::isConfigurationLocationsBeanType)
			// store configured resources in locationsByOrder
			.forEachOrdered(this.configurationLocationsBeanTypeToLocations(locationsByOrder));
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Aggregated configuration before merging: {}",
					Joiner.on(", ").withKeyValueSeparator(":").join(locationsByOrder.asMap()));
		}
		
		// order provided configurations
		List<Resource> locations = Lists.newArrayList();
		List<Integer> orders = Lists.newArrayList(locationsByOrder.keySet());
		Collections.sort(orders);
		for (Integer order : orders) {
			locations.addAll(locationsByOrder.get(order));
		}
		LOGGER.info("Aggregated configuration after merging: {}", Joiner.on(", ").join(locations));
		
		return locations;
	}

	/**
	 * <p>Return list of overriding configurations resources computed from bootstraped configuration.</p>
	 * 
	 * <p>Non existing or non readable locations are either ignored or throw an exception.</p>.
	 * 
	 * <p>Returned list is not null and can be empty.</p>
	 * 
	 * @see #notFoundLocationThrowError
	 */
	public static List<Resource> getLocationsFromBootstrapConfiguration(ApplicationContext applicationContext,
			boolean notFoundLocationThrowError) {
		List<Resource> locations = Lists.newArrayList();
		Environment environment = applicationContext.getEnvironment();
		if (environment.containsProperty(IGLOO_PROFILES_LOCATIONS_PROPERTY)) {
			@SuppressWarnings("unchecked")
			final List<String> profileLocations =
					(List<String>) environment.getProperty(IGLOO_PROFILES_LOCATIONS_PROPERTY, List.class);
			List<Resource> profileResources = profileLocations.stream()
					.map(resourceFromLocation(applicationContext, notFoundLocationThrowError)).filter(Objects::nonNull)
					.collect(Collectors.toList());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Loaded locations from {} value ({}): {}",
						// annotated type
						IGLOO_PROFILES_LOCATIONS_PROPERTY,
						environment.getProperty(IGLOO_PROFILES_LOCATIONS_PROPERTY),
						// list of loaded files
						Joiner.on(", ").join(profileResources.stream()
								.map(ApplicationConfigurerBeanFactoryPostProcessor::toURL)
								.collect(Collectors.toList())));
			}
			
			locations.addAll(profileResources);
		} else {
			throw new IllegalStateException(String.format("No bootstrap configuration found for %s. " +
					"Configure %s as a spring context initializer.",
					IGLOO_PROFILES_LOCATIONS_PROPERTY,
					AbstractExtendedApplicationContextInitializer.class));
		}
		return locations;
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
	private Consumer<Class<?>> configurationLocationsBeanTypeToLocations(Multimap<Integer, Resource> locations) {
		return beanType -> {
			ConfigurationLocations annotation = beanType.getAnnotation(ConfigurationLocations.class);
			
			List<Resource> resources = Arrays.stream(annotation.locations())
					.map(resourceFromLocation(applicationContext, notFoundLocationThrowError)).filter(Objects::nonNull)
					.collect(Collectors.toList());
			locations.putAll(annotation.order(), resources);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Loaded locations from {} with order {}: {}",
						// annotated type
						beanType.getSimpleName(),
						annotation.order(),
						// list of loaded files
						Joiner.on(", ").join(resources.stream()
								.map(ApplicationConfigurerBeanFactoryPostProcessor::toURL)
								.collect(Collectors.toList())));
			}
		};
	}

	/**
	 * Resource to a printable name.
	 */
	private static final String toURL(Resource resource) {
		try {
			return resource.getURL().toString();
		} catch (IOException e) {
			// not reachable code as we filter location before constructing resources
			return String.format("%s", resource.getFilename());
		}
	}

	/**
	 * <p>Map a location to the corresponding resource. If resource is not readable, either throw an exception or
	 * ignore the location. Ignored locations map to null.</p>
	 * 
	 * <p>If you map a collection, you need to filter null values after mapping.</p>
	 */
	private static Function2<String, Resource> resourceFromLocation(ApplicationContext applicationContext,
			boolean throwErrorIfNotReadable) {
		return location -> {
			String resolvedLocation = applicationContext.getEnvironment().resolveRequiredPlaceholders(location);
			if (applicationContext.getResource(resolvedLocation).isReadable()) {
				LOGGER.debug("Configuration {} detected and added", location);
				return applicationContext.getResource(resolvedLocation);
			} else if (throwErrorIfNotReadable) {
				throw new IllegalStateException(String.format("Configuration %s does not exist or is not readable", location));
			} else {
				LOGGER.warn("Configuration {} not readable; ignored", location);
				return null;
			}
		};
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
	 * We need this {@link BeanFactoryPostProcessor} to be processed before {@link PropertySourcesPlaceholderConfigurer}
	 */
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 500;
	}

}
