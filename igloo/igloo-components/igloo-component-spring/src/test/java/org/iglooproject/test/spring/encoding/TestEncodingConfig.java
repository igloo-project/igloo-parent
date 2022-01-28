package org.iglooproject.test.spring.encoding;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.config.bootstrap.spring.env.CompositePropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Load configuration for mail encoding.
 */
@Configuration
// check composite resource encoding (multiple loaded files)
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "composite:classpath:encoding-test.properties,classpath:encoding-test2.properties",
	factory = CompositePropertySourceFactory.class,
	encoding = "UTF-8"
)
// check individual resource loading encoding
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "classpath:encoding-test-alone.properties",
	encoding = "UTF-8"
)
public class TestEncodingConfig {

}
