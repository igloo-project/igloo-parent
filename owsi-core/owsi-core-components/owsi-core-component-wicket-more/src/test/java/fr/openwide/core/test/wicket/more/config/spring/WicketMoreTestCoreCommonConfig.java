package fr.openwide.core.test.wicket.more.config.spring;

import java.net.MalformedURLException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;

@Configuration
@ConfigurationLocations
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class WicketMoreTestCoreCommonConfig {

	public static final String PROFILE_TEST = "test";
	
	private static final String UTF8 = "UTF-8";

	/**
	 * L'obtention du configurer doit être statique.
	 */
	@Bean(name = { "configurer" })
	public static WicketMoreTestConfigurer environment(ConfigurableApplicationContext context) throws MalformedURLException {
		WicketMoreTestConfigurer configurer = new WicketMoreTestConfigurer();
		configurer.setFileEncoding(UTF8);
		
		return configurer;
	}
}