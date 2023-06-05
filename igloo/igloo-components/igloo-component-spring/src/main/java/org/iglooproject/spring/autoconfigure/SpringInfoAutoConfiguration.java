package org.iglooproject.spring.autoconfigure;

import org.iglooproject.config.bootstrap.spring.ManifestPropertySourceBeanFactoryPostProcessor;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConditionalOnBean(ManifestPropertySourceBeanFactoryPostProcessor.class)
@ManifestPropertySource(prefix = "igloo.component-spring")
@PropertySource(
	name = IglooPropertySourcePriority.COMPONENT,
	value = "classpath:igloo-component-spring.properties",
	encoding = "UTF-8"
)
public class SpringInfoAutoConfiguration {

}
