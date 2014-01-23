package fr.openwide.core.spring.config;


/**
 * @see AbstractExtendedApplicationContextInitializer
 * 
 * La configuration chargée est classpath:configuration-test.properties et classpath:configuration-test-{user.name}.properties.
 * La configuration log4j est chargée à partir de la propriété log4j.configurationLocations.
 */
public class ExtendedTestApplicationContextInitializer extends AbstractExtendedApplicationContextInitializer {

	@Override
	public String getMainConfigurationLocation() {
		return "classpath:configuration-test.properties";
	}

	@Override
	public String getCustomConfigurationLocation() {
		return "classpath:configuration-test-" + System.getProperty("user.name") + ".properties";
	}

}
