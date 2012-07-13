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
 */
public class ExtendedApplicationContextInitializer<C extends ConfigurableApplicationContext> implements ApplicationContextInitializer<C> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedApplicationContextInitializer.class);

	private static final String SPRING_LOG4J_CONFIGURATION = "log4j.configurationLocations";

	@Override
	public void initialize(C applicationContext) {
		try {
			// définition d'un environnement par défaut (permet de configurer l'application avant le chargement
			// des beans) ; sert entre autres pour la définiton des profils spring à charger.
			applicationContext.getEnvironment().getPropertySources().addFirst(new ResourcePropertySource("classpath:configuration.properties"));
			String customLocation = "classpath:configuration-" + System.getProperty("user.name") + ".properties";
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
						hasSource = true;
						ResourcePropertySource source = new ResourcePropertySource(applicationContext.getResource(location));
						sources.addFirst(source);
						propertyNames.addAll(Arrays.asList(source.getPropertyNames()));
					} else {
						LOGGER.warn(location + " non trouvée.");
					}
				}
				
				if (hasSource) {
					PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(sources);
					resolver.setPlaceholderPrefix("#{");
					resolver.setPlaceholderSuffix("}");
					Properties properties = new Properties();
					for (String propertyName : propertyNames) {
						if (resolver.containsProperty(propertyName)) {
							properties.put(propertyName, resolver.getProperty(propertyName));
						} else {
							LOGGER.warn("Propriété " + propertyName + " non trouvée.");
						}
					}
					properties.setProperty("log4j.reset", "true");
					PropertyConfigurator.configure(properties);
				} else {
					LOGGER.error("Aucune configuration n'a pu être chargée parmi " + locationsString);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Erreur de chargement de la source de profil", e);
		}
	}

}
