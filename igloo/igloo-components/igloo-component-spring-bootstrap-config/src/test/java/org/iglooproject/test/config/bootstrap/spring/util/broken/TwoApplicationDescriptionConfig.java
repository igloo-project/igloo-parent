package org.iglooproject.test.config.bootstrap.spring.util.broken;

import org.iglooproject.config.bootstrap.spring.ApplicationConfigurerBeanFactoryPostProcessor;
import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.iglooproject.test.config.bootstrap.spring.util.BootstrapSpringConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ BootstrapSpringConfig.class, TwoApplicationDescriptionConfig.Class1.class, TwoApplicationDescriptionConfig.Class2.class })
public class TwoApplicationDescriptionConfig {

	@Bean
	public static ApplicationConfigurerBeanFactoryPostProcessor configurerBeanFactoryPostProcessor() {
		return new ApplicationConfigurerBeanFactoryPostProcessor(false);
	}

	@ApplicationDescription(name = "application1")
	@Configuration
	public static class Class1 {
		// NOSONAR
	}

	@ApplicationDescription(name = "application2")
	@Configuration
	public static class Class2 {
		// NOSONAR
	}

}
