package fr.openwide.core.spring.config;


/**
 * @see AbstractExtendedApplicationContextInitializer
 * 
 * La configuration chargée est classpath:configuration.properties et classpath:configuration-{user.name}.properties.
 * La configuration log4j est chargée à partir de la propriété log4j.configurationLocations.
 */
public class ExtendedApplicationContextInitializer extends AbstractExtendedApplicationContextInitializer {

	@Override
	public String getMainConfigurationLocation() {
		return "classpath:configuration.properties";
	}

	@Override
	public String getCustomConfigurationLocation() {
		return "classpath:configuration-" + System.getProperty("user.name") + ".properties";
	}

}
