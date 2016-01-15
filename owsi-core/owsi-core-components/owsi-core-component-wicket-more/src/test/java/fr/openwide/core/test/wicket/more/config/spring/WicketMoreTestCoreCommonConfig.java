package fr.openwide.core.test.wicket.more.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.openwide.core.spring.config.CorePropertyPlaceholderConfigurer;

@Configuration
// fonctionnement de l'annotation @Transactional
@EnableTransactionManagement
public class WicketMoreTestCoreCommonConfig {

	public static final String PROFILE_TEST = "test";
	
	public static CorePropertyPlaceholderConfigurer corePropertyPlaceholderConfigurer() {
		return new CorePropertyPlaceholderConfigurer();
	}
}