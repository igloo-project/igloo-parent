package org.iglooproject.test.config.bootstrap.spring;

import org.iglooproject.config.bootstrap.spring.ApplicationConfigurerBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

	@Bean
	public ApplicationConfigurerBeanFactoryPostProcessor applicationConfigurer() {
		return new ApplicationConfigurerBeanFactoryPostProcessor();
	}
}
