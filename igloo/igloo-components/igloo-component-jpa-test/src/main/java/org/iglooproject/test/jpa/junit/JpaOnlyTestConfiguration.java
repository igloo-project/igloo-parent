package org.iglooproject.test.jpa.junit;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "classpath:igloo-component-jpa-test/jpa.properties",
	encoding = "UTF-8"
)
public class JpaOnlyTestConfiguration {
}