package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.ApplicationConfigurerBeanFactoryPostProcessor;
import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ApplicationDescription(name = "application-test")
public class SpringConfig {

	/**
	 * static modifier is mandatory !
	 */
	@Bean
	public static ApplicationConfigurerBeanFactoryPostProcessor applicationConfigurer() {
		return new ApplicationConfigurerBeanFactoryPostProcessor(false);
	}
	
	@Bean
	public PropertySourcesPlaceholderConfigurer configurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
