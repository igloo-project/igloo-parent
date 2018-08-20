package org.iglooproject.test.config.bootstrap.spring.util.broken;

import org.iglooproject.config.bootstrap.spring.ApplicationConfigurerBeanFactoryPostProcessor;
import org.iglooproject.test.config.bootstrap.spring.util.BootstrapSpringConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ BootstrapSpringConfig.class })
public class NoApplicationDescriptionConfig {

	@Bean
	public static ApplicationConfigurerBeanFactoryPostProcessor configurerBeanFactoryPostProcessor() {
		return new ApplicationConfigurerBeanFactoryPostProcessor(false);
	}

}
