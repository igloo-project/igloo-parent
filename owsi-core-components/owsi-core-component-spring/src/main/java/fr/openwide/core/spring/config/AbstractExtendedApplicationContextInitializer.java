package fr.openwide.core.spring.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.ResourcePropertySource;

import fr.openwide.core.spring.util.StringUtils;

/**
 * Cet initializer traite les deux sujets suivants :
 *  - prise en compte des fichiers classpath:configuration.properties et classpath:configuration-${user.name}.properties
 *  pour l'initialisation de l'environnement (permet entre autres de prendre en compte les profils spring à la volée)
 *  - initialisation log4j à l'aide de la propriété log4j.configurationLocations de l'environnement
 * 
 * Note mise à jour le 5 juillet 2013 : avant, on avait un type générique, mais ce n'était pas compatible avec la mise
 * en place du ContextConfiguration#initializers(). Transformation en type non générique.
 */
public abstract class AbstractExtendedApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExtendedApplicationContextInitializer.class);

	private static final String SPRING_LOG4J_CONFIGURATION = "log4j.configurationLocations";

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		try {
			// définition d'un environnement par défaut (permet de configurer l'application avant le chargement
			// des beans) ; sert entre autres pour la définiton des profils spring à charger.
			ResourcePropertySource resourcePropertySource = new ResourcePropertySource(getMainConfigurationLocation());
			
			applicationContext.getEnvironment().getPropertySources().addFirst(resourcePropertySource);
			String customLocation = getCustomConfigurationLocation();
			if (applicationContext.getResource(customLocation).exists()) {
				applicationContext.getEnvironment().getPropertySources().addFirst(new ResourcePropertySource(customLocation));
			}
			
			// configuration de log4j ; permet de spécifier plusieurs fichiers de propriétés qui seront combinés entre
			// eux pour faire le fichier global de configuration.
			if (applicationContext.getEnvironment().getProperty(SPRING_LOG4J_CONFIGURATION) != null) {
				String locationsString = applicationContext.getEnvironment().getProperty(SPRING_LOG4J_CONFIGURATION);
				List<String> locations = StringUtils.splitAsList(locationsString, ",");
				MutablePropertySources sources = new MutablePropertySources();
				boolean hasSource = false;
				List<String> propertyNames = new ArrayList<String>();
				for (String location : locations) {
					if (applicationContext.getResource(location).exists()) {
						LOGGER.info(String.format("Log4j : %1$s added", location));
						hasSource = true;
						ResourcePropertySource source = new ResourcePropertySource(applicationContext.getResource(location));
						sources.addFirst(source);
						propertyNames.addAll(Arrays.asList(source.getPropertyNames()));
					} else {
						LOGGER.warn(String.format("Log4j : %1$s not found", location));
					}
				}
				
				if (hasSource) {
					PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(sources);
					resolver.setPlaceholderPrefix("#{");
					resolver.setPlaceholderSuffix("}");
					Properties properties = new Properties();
					for (String propertyName : propertyNames) {
						if (resolver.containsProperty(propertyName)) {
							LOGGER.debug(String.format("Log4j : property resolved %1$s -> %2$s", propertyName, resolver.getProperty(propertyName)));
							properties.put(propertyName, resolver.getProperty(propertyName));
						} else {
							LOGGER.warn(String.format("Log4j : property %1$s cannot be resolved", propertyName));
						}
					}
					properties.setProperty("log4j.reset", "true");
					PropertyConfigurator.configure(properties);
				} else {
					LOGGER.warn("Log4j : no additional files configured in %1$s", locationsString);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error loading configuration files", e);
		}
	}

	/**
	 * Emplacement de la configuration principale.
	 */
	public abstract String getMainConfigurationLocation();

	/**
	 * Emplacement de la configuration spécifique (prise en compte en priorité).
	 */
	public abstract String getCustomConfigurationLocation();

}
