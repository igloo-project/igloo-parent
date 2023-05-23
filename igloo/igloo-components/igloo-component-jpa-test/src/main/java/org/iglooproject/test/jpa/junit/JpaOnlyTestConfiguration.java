package org.iglooproject.test.jpa.junit;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.config.spring.IglooJpaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(IglooJpaConfiguration.class)
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "classpath:igloo-component-jpa-test/jpa.properties",
	encoding = "UTF-8"
)
public class JpaOnlyTestConfiguration {
}