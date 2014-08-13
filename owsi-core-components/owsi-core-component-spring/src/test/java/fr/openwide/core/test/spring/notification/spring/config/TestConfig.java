package fr.openwide.core.test.spring.notification.spring.config;

import java.net.MalformedURLException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.spring.annotation.ApplicationConfigurerBeanFactoryPostProcessor;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;

@Configuration
@ApplicationDescription(name = "TestNotification")
@ConfigurationLocations(locations = "classpath:notification-test.properties")
@Import({ NotificationTestConfig.class })
public class TestConfig {
	
	@Bean(name = { "configurer" })
	public static CoreConfigurer environment(ConfigurableApplicationContext context) throws MalformedURLException {
		return new CoreConfigurer();
	}
	
	@Bean
	public static ApplicationConfigurerBeanFactoryPostProcessor applicationConfigurer() {
		return new ApplicationConfigurerBeanFactoryPostProcessor();
	}

}
