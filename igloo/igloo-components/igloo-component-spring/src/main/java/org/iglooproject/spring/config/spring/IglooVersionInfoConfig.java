package org.iglooproject.spring.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ManifestPropertySource(prefix = "igloo.component-spring")
@PropertySource(name = IglooPropertySourcePriority.FRAMEWORK, value = "classpath:igloo-component-spring.properties")
public class IglooVersionInfoConfig {

}
