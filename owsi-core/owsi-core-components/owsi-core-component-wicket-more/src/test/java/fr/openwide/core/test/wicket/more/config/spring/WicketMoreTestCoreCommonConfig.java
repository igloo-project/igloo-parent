package fr.openwide.core.test.wicket.more.config.spring;

import java.net.MalformedURLException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.openwide.core.spring.config.CorePropertyPlaceholderConfigurer;

@Configuration
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class WicketMoreTestCoreCommonConfig {

	public static final String PROFILE_TEST = "test";
	
	/**
	 * L'obtention du configurer doit être statique.
	 */
	@Bean(name = { "configurer" })
	public WicketMoreTestConfigurer environment(ConfigurableApplicationContext context) throws MalformedURLException {
		return new WicketMoreTestConfigurer();
	}
	
	public static CorePropertyPlaceholderConfigurer corePropertyPlaceholderConfigurer() {
		return new CorePropertyPlaceholderConfigurer();
	}
}